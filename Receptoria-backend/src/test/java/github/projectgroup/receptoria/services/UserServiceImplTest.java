package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.domain.dtos.UserDTO;
import github.projectgroup.receptoria.domain.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.domain.enities.User;
import github.projectgroup.receptoria.domain.mappers.UserMapper;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImpl(userRepository,userMapper);

        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return UserDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .build();
        });
    }

    @Test
    void getById_shouldReturnSuccess_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Іван");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Result<UserDTO> result = userService.getById(1L);

        assertTrue(result.isSuccess());
        assertNotNull(result.getValue());
    }

    @Test
    void getById_shouldReturnFailure_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Result<UserDTO> result = userService.getById(1L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        assertNull(result.getValue());
    }

    @Test
    void updateById_shouldUpdateFieldsAndReturnSuccess() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Old");

        UserUpdateRequest req = new UserUpdateRequest();
        req.setFirstName("New");
        req.setEmail("new@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        Result<UserDTO> result = userService.updateById(1L, req);

        assertTrue(result.isSuccess());
        assertEquals("New", user.getFirstName());
        assertEquals("new@email.com", user.getEmail());
        assertTrue(result.getMessage().contains("2"));
    }

    @Test
    void updateById_shouldReturnFailure_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Result<UserDTO> result = userService.updateById(1L, new UserUpdateRequest());

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
    }

    @Test
    void findBy_shouldReturnMappedPage() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByFullNameAndCategories(eq("Іван"), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user)));

        Page<UserDTO> page = userService.findBy("Іван", new github.projectgroup.receptoria.domain.enums.MealCategory[]{}, Pageable.unpaged());

        assertEquals(1, page.getContent().size());
    }
}