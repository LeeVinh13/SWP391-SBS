package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.SlotDetailResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Slot;
import vn.vinhdeptrai.skincarebookingsystem.entity.SlotDetail;
import vn.vinhdeptrai.skincarebookingsystem.entity.Therapist;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;
import vn.vinhdeptrai.skincarebookingsystem.mapper.SlotDetailMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.SlotDetailRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.TherapistRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotDetailService {

}
