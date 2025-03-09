package vn.vinhdeptrai.skincarebookingsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.vinhdeptrai.skincarebookingsystem.enums.SlotStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlotDetailResponse {
    int slotId;
    int therapistId;
    String therapistName;
    SlotStatus status;

}
