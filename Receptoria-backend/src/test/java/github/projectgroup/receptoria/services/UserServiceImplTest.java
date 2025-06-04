package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enums.MealCategory;
import github.projectgroup.receptoria.model.mappers.UserMapper;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.utils.result.BadArgumentsCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetById_UserExists() {
        User user = User.builder().id(1L).build();
        UserDTO dto = UserDTO.builder().id(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        Result<UserDTO> result = userService.getById(1L);

        assertTrue(result.isSuccess());
        assertEquals(dto, result.getValue());
        assertTrue(result.isCaseInstanceOf(SuccessCase.class));
    }

    @Test
    void testGetById_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Result<UserDTO> result = userService.getById(2L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
    }

    @Test
    void testUpdateById_SuccessfulUpdate() {
        User existing = User.builder()
                .id(3L)
                .firstName("OldFirst")
                .lastName("OldLast")
                .email("old@example.com")
                .mainMealCategories(Collections.emptyList())
                .build();
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("NewFirst");
        request.setLastName("NewLast");
        request.setEmail("new@example.com");
        request.setMainMealCategories(Arrays.asList(MealCategory.BREAKFAST));
        User saved = User.builder()
                .id(3L)
                .firstName("NewFirst")
                .lastName("NewLast")
                .email("new@example.com")
                .mainMealCategories(Arrays.asList(MealCategory.BREAKFAST))
                .build();
        UserDTO dto = UserDTO.builder().id(3L).build();
        when(userRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(dto);

        Result<UserDTO> result = userService.updateById(3L, request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User captured = captor.getValue();
        assertEquals("NewFirst", captured.getFirstName());
        assertEquals("NewLast", captured.getLastName());
        assertEquals("new@example.com", captured.getEmail());
        assertEquals(Arrays.asList(MealCategory.BREAKFAST), captured.getMainMealCategories());
        assertTrue(result.isSuccess());
        assertEquals(dto, result.getValue());
        assertTrue(result.isCaseInstanceOf(SuccessCase.class));
    }

    @Test
    void testUpdateById_EmailAlreadyExists() {
        User existing = User.builder().id(4L).email("old2@example.com").build();
        User conflict = User.builder().id(5L).email("conflict@example.com").build();
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("conflict@example.com");
        when(userRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("conflict@example.com")).thenReturn(Optional.of(conflict));

        Result<UserDTO> result = userService.updateById(4L, request);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(BadArgumentsCase.class));
    }

    @Test
    void testUpdateById_UserNotFound() {
        UserUpdateRequest request = new UserUpdateRequest();
        when(userRepository.findById(6L)).thenReturn(Optional.empty());

        Result<UserDTO> result = userService.updateById(6L, request);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
    }

    @Test
    void testUpdateById_NoFieldsToUpdate() {
        User existing = User.builder()
                .id(7L)
                .firstName("F")
                .lastName("L")
                .email("e@example.com")
                .mainMealCategories(Collections.emptyList())
                .build();
        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName(null);
        request.setLastName(null);
        request.setEmail(null);
        request.setMainMealCategories(Collections.emptyList());
        User saved = existing;
        UserDTO dto = UserDTO.builder().id(7L).build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(saved);
        when(userMapper.toDto(saved)).thenReturn(dto);

        Result<UserDTO> result = userService.updateById(7L, request);

        verify(userRepository).save(existing);
        assertTrue(result.isSuccess());
        assertEquals(dto, result.getValue());
        assertTrue(result.isCaseInstanceOf(SuccessCase.class));
    }

    @Test
    void testFindBy_ReturnsPage() {
        User user1 = User.builder().id(8L).build();
        User user2 = User.builder().id(9L).build();
        Pageable pageable = Pageable.unpaged();
        Page<User> page = new PageImpl<>(Arrays.asList(user1, user2));
        UserDTO dto1 = UserDTO.builder().id(8L).build();
        UserDTO dto2 = UserDTO.builder().id(9L).build();
        when(userRepository.findByFullNameAndCategories("name", new MealCategory[]{MealCategory.LUNCH}, pageable))
                .thenReturn(page);
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        Page<UserDTO> result = userService.findBy("name", new MealCategory[]{MealCategory.LUNCH}, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));
    }
}
