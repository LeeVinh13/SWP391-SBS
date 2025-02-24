package vn.vinhdeptrai.skincarebookingsystem.config;

import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.IntrospectRequest;
import vn.vinhdeptrai.skincarebookingsystem.service.AuthenticationService;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${signerkey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;
    /*
        giải mã JWT Token, xác thực JWT = secret key(SIGNERKEY)  đảm bảo phù hợp
    */
    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            log.warn(token);
            var response = authenticationService.introspect(IntrospectRequest.builder()
                                                                                .token(token)
                                                                                .build());
            if (!response.isValid())
                throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        // decode(token) trả về đối tượng JWT có 3 tp: 1.Header, 2.Payload, 3.Expiration Time
        return nimbusJwtDecoder.decode(token);
    }
}
