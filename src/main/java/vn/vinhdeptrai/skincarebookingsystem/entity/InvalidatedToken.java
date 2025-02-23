package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalidatedToken")
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;

}
