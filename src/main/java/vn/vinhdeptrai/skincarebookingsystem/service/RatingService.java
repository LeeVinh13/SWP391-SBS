package vn.vinhdeptrai.skincarebookingsystem.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RatingRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.RatingResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Appointment;
import vn.vinhdeptrai.skincarebookingsystem.entity.Rating;
import vn.vinhdeptrai.skincarebookingsystem.entity.User;
import vn.vinhdeptrai.skincarebookingsystem.enums.AppointmentStatus;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.RatingMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.AppointmentRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.RatingRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingService {
    RatingRepository ratingRepository;
    AppointmentRepository appointmentRepository;
    RatingMapper ratingMapper;
    UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    
    public RatingResponse createRating(int appointmentId, RatingRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        if (ratingRepository.existsByAppointmentId(appointmentId)) {
            throw new AppException(ErrorCode.RATING_ALREADY_EXIST);
        }

        if (appointment.getAppointmentStatus() != AppointmentStatus.APPROVED) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_APPROVED);
        }

        User currentUser = getCurrentUser();
        if (appointment.getUser().getId() != currentUser.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Rating rating = ratingMapper.toRating(request);
        rating = Rating.builder()
        .appointment(appointment)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
        
        return ratingMapper.toRatingResponse(ratingRepository.save(rating));
    }

    public RatingResponse getRatingByAppointment(int appointmentId) {
        Rating rating = ratingRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));

        User currentUser = getCurrentUser();
        if (rating.getAppointment().getUser().getId() != currentUser.getId()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        
        return ratingMapper.toRatingResponse(rating);
    }

    public List<RatingResponse> getRatingByService(int serviceId) {
        List<Rating> ratings = ratingRepository.findByServiceId(serviceId);
        return ratings.stream()
        .map(ratingMapper::toRatingResponse)
        .collect(Collectors.toList());
    }

    public List<RatingResponse> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        return ratings.stream()
        .map(ratingMapper::toRatingResponse)
        .collect(Collectors.toList());
    }

    public void deleteRating(int ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
        .orElseThrow(() -> new AppException(ErrorCode.RATING_NOT_FOUND));

        ratingRepository.delete(rating);
    }
}