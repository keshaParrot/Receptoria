package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.model.enities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByCode(String code);
}
