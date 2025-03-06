package vn.vinhdeptrai.skincarebookingsystem.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.vinhdeptrai.skincarebookingsystem.constant.PredefinedRole;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AuthenticationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.IntrospectRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.LogoutRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RegisterRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AuthenticationResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.IntrospectResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RegisterResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.InvalidatedToken;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.repository.InvalidatedTokenRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.RoleRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.UserRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    //SIGNER KEY là chuỗi secret key, dùng để ký JWT or mã hóa data
    @NonFinal
    @Value("${signerkey}")
    String SIGNERKEY;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;
    // hàm xác thực khi người dùng đăng nhập hệ thống
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
//            boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
//
//        if (!authenticated) {
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }

        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }
    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .fullname(registerRequest.getFullname())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        Role role = roleRepository.findByName(PredefinedRole.USER_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(Set.of(role));
        userRepository.save(user);
        return RegisterResponse.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .fullname(registerRequest.getFullname())
                .build();
    }
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        var signToken = verifyToken(logoutRequest.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expirationTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var token = introspectRequest.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }
    //tạo JWT Token
    public String generateToken(User user) {
        //header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("duahau.mieng")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNERKEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNERKEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if(expirationDate.before(new Date()) && !verified) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRole())){
            user.getRole().forEach(role ->stringJoiner.add(role.getName()));
        }
//         ADMIN STAFF USER
//        for (Role role : user.getRole()) {
//            stringJoiner.add(role.getName());
//        }
        return stringJoiner.toString();
    }
}

