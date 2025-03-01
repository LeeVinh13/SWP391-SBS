package vn.vinhdeptrai.skincarebookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//mỗi subclass có 1 bảng riêng, nối với bảng "user" bằng khóa ngoại
@Inheritance(strategy = InheritanceType.JOINED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;
    String password;
    String email;
    String fullname;
    String phone;
    @ManyToMany
    @JoinTable(
            name="user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name ="roleId")
    )
    Set<Role> role;
    @OneToMany(mappedBy = "user")
    List<Appointment> appointments;

}
