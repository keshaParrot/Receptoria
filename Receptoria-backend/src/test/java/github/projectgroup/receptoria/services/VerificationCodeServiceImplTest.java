package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.VerificationCodeRepository;
import github.projectgroup.receptoria.utils.result.InvalidVerificationCode;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationCodeServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @InjectMocks
    private VerificationCodeServiceImpl service;

    @Captor
    private ArgumentCaptor<VerificationCode> codeCaptor;

    @Test
    void testGenerateVerificationCode_UserNotFound() {
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Result<VerificationCode> result = service.generateVerificationCode(email, SendTo.EMAIL, VerificationCodeType.VERIFICATION_MAIL);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(verificationCodeRepository, never()).save(any());
    }

    @Test
    void testGenerateVerificationCode_VerificationMail() {
        String email = "user@mail.com";
        User user = User.builder().id(1L).email(email).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime before = LocalDateTime.now();
        Result<VerificationCode> result = service.generateVerificationCode(email, SendTo.EMAIL, VerificationCodeType.VERIFICATION_MAIL);
        LocalDateTime after = LocalDateTime.now();

        assertTrue(result.isSuccess());
        VerificationCode saved = result.getValue();
        assertNotNull(saved.getCode());
        assertEquals(user, saved.getGeneratedFor());
        assertEquals(VerificationCodeType.VERIFICATION_MAIL, saved.getType());
        assertEquals(SendTo.EMAIL, saved.getChannel());
        assertNotNull(saved.getExpirationDate());

        Duration lower = Duration.between(before.plusDays(29), saved.getExpirationDate());
        Duration upper = Duration.between(saved.getExpirationDate(), after.plusMonths(1).plusMinutes(1));
        assertFalse(lower.isNegative());
        assertFalse(upper.isNegative());

        verify(verificationCodeRepository).save(saved);
    }

    @Test
    void testGenerateVerificationCode_ResetPassword() {
        String email = "reset@domain.com";
        User user = User.builder().id(2L).email(email).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime before = LocalDateTime.now();
        Result<VerificationCode> result = service.generateVerificationCode(email, SendTo.SMS, VerificationCodeType.RESET_PASSWORD);
        LocalDateTime after = LocalDateTime.now();

        assertTrue(result.isSuccess());
        VerificationCode saved = result.getValue();
        assertNotNull(saved.getCode());
        assertEquals(user, saved.getGeneratedFor());
        assertEquals(VerificationCodeType.RESET_PASSWORD, saved.getType());
        assertEquals(SendTo.SMS, saved.getChannel());
        assertNotNull(saved.getExpirationDate());

        Duration lower = Duration.between(before.plusHours(7), saved.getExpirationDate());
        Duration upper = Duration.between(saved.getExpirationDate(), after.plusHours(8).plusMinutes(1));
        assertFalse(lower.isNegative());
        assertFalse(upper.isNegative());

        verify(verificationCodeRepository).save(saved);
    }

    @Test
    void testVerifyVerificationCode_Success() {
        String codeString = "ABC123";
        VerificationCode code = VerificationCode.builder()
                .code(codeString)
                .type(VerificationCodeType.RESET_MAIL)
                .expirationDate(LocalDateTime.now().plusMinutes(10))
                .build();
        when(verificationCodeRepository.findByCode(codeString)).thenReturn(Optional.of(code));

        Result<VerificationCode> result = service.verifyVerificationCode(codeString, VerificationCodeType.RESET_MAIL);

        assertTrue(result.isSuccess());
        assertEquals(code, result.getValue());
        assertTrue(result.isCaseInstanceOf(SuccessCase.class));
    }

    @Test
    void testVerifyVerificationCode_Failure_NotFound() {
        String codeString = "NOTEXIST";
        when(verificationCodeRepository.findByCode(codeString)).thenReturn(Optional.empty());

        Result<VerificationCode> result = service.verifyVerificationCode(codeString, VerificationCodeType.VERIFICATION_MAIL);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(InvalidVerificationCode.class));
    }

    @Test
    void testVerifyVerificationCode_Failure_Expired() {
        String codeString = "EXPIRE";
        VerificationCode code = VerificationCode.builder()
                .code(codeString)
                .type(VerificationCodeType.VERIFICATION_MAIL)
                .expirationDate(LocalDateTime.now().minusMinutes(1))
                .build();
        when(verificationCodeRepository.findByCode(codeString)).thenReturn(Optional.of(code));

        Result<VerificationCode> result = service.verifyVerificationCode(codeString, VerificationCodeType.VERIFICATION_MAIL);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(InvalidVerificationCode.class));
    }

    @Test
    void testVerifyVerificationCode_Failure_WrongType() {
        String codeString = "WRONGTYPE";
        VerificationCode code = VerificationCode.builder()
                .code(codeString)
                .type(VerificationCodeType.RESET_PASSWORD)
                .expirationDate(LocalDateTime.now().plusMinutes(5))
                .build();
        when(verificationCodeRepository.findByCode(codeString)).thenReturn(Optional.of(code));

        Result<VerificationCode> result = service.verifyVerificationCode(codeString, VerificationCodeType.VERIFICATION_MAIL);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(InvalidVerificationCode.class));
    }

    @Test
    void testDeleteById_CallsRepository() {
        Long id = 5L;

        service.deleteById(id);

        verify(verificationCodeRepository).deleteById(id);
    }
}
