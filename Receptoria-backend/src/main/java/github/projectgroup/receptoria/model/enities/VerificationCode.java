package github.projectgroup.receptoria.model.enities;

import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode implements Ownable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @ManyToOne
    private User generatedFor;
    @Enumerated(EnumType.STRING)
    private VerificationCodeType type;
    private LocalDateTime expirationDate;
    private SendTo channel;


    @Override
    public Long getOwnerIdentifier() {
        return generatedFor.getId();
    }
}
