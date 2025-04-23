package github.projectgroup.receptoria.domain.enities;

import github.projectgroup.receptoria.domain.enums.VerificationCodeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @ManyToOne
    private User generatedFor;
    @Enumerated(EnumType.STRING)
    private VerificationCodeType type;


}
