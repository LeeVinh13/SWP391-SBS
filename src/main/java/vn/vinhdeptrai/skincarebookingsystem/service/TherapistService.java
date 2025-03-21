package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.vinhdeptrai.skincarebookingsystem.constant.PredefinedRole;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.TherapistRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.TherapistResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Role;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.TherapistMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.RoleRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.TherapistRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.UserRepository;
import vn.vinhdeptrai.skincarebookingsystem.util.CloudinaryUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TherapistService {
    TherapistRepository therapistRepository;
    UserRepository userRepository;
    TherapistMapper therapistMapper;
    PasswordEncoder passwordEncoder;
    CloudinaryUtil cloudinaryUtil;
    RoleRepository roleRepository;

    public List<TherapistResponse> getAll() {
        List<Therapist> therapists = therapistRepository.findAll();
        if(therapists.isEmpty()){
            throw new AppException(ErrorCode.THERAPIST_NOT_FOUND);
        }
        return therapists.stream().map(therapistMapper::toTherapistResponse).collect(Collectors.toList());
    }
    public TherapistResponse getById(Integer id) {
        Therapist therapist = therapistRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        return therapistMapper.toTherapistResponse(therapist);
    }
    public TherapistResponse create(TherapistRequest therapistRequest, MultipartFile file) throws IOException {
        if(userRepository.existsByUsername(therapistRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Role therapistRole = roleRepository.findByName(PredefinedRole.THERAPIST_ROLE).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        Therapist therapist = therapistMapper.toTherapist(therapistRequest);
//        therapist.setPassword(passwordEncoder.encode(therapistRequest.getPassword()));
        therapist.setImage(cloudinaryUtil.uploadImage(file));
        therapist.setRole(Set.of(therapistRole));

        return therapistMapper.toTherapistResponse(therapistRepository.save(therapist));
    }
    public TherapistResponse update(TherapistRequest therapistRequest, MultipartFile file, int id) throws IOException {
        Therapist therapist = therapistRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.THERAPIST_NOT_FOUND)
        );
        therapistMapper.updateTherapist(therapist,therapistRequest);
//        therapist.setPassword(passwordEncoder.encode(therapistRequest.getPassword()));
        if(file != null){
            therapist.setImage(cloudinaryUtil.uploadImage(file));
        }
        return therapistMapper.toTherapistResponse(therapistRepository.save(therapist));
    }
    public void delete(int id) throws AppException {
        if(!userRepository.existsById(id)){
            throw new AppException(ErrorCode.THERAPIST_NOT_FOUND);
        }
        therapistRepository.deleteById(id);
    }


}
