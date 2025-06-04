package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.enities.Follow;
import github.projectgroup.receptoria.model.enities.User;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void testToDto_AllFieldsPopulated() {
        User user = User.builder()
                .id(42L)
                .firstName("Ivan")
                .lastName("Petrov")
                .email("ivan.petrov@example.com")
                .username("ivanp")
                .build();

        List<Follow> dummyFollowers = List.of(new Follow(),new Follow(),new Follow());
        user.setFollowers(dummyFollowers);

        List<Follow> dummyFollows = List.of(new Follow(),new Follow());
        user.setFollows(dummyFollows);

        UserDTO dto = UserMapper.INSTANCE.toDto(user);

        assertEquals(42L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Petrov", dto.getLastName());
        assertEquals("ivan.petrov@example.com", dto.getEmail());
        assertEquals("ivanp", dto.getUsername());

        assertEquals(3, dto.getFollowers());
        assertEquals(2, dto.getFollowing());
    }

    @Test
    void testToDto_NullLists() {
        User user = User.builder()
                .id(7L)
                .firstName("Olha")
                .lastName("Shevchenko")
                .email("olha.shevchenko@example.com")
                .username("olhas")
                .build();
        user.setFollowers(null);
        user.setFollows(null);

        UserDTO dto = UserMapper.INSTANCE.toDto(user);

        assertEquals(7L, dto.getId());
        assertEquals("Olha", dto.getFirstName());
        assertEquals("Shevchenko", dto.getLastName());
        assertEquals("olha.shevchenko@example.com", dto.getEmail());
        assertEquals("olhas", dto.getUsername());

        assertEquals(0, dto.getFollowers());
        assertEquals(0, dto.getFollowing());
    }

    @Test
    void testToDto_EmptyLists() {
        User user = User.builder()
                .id(100L)
                .firstName("Taras")
                .lastName("Bondarenko")
                .email("taras.bondarenko@example.com")
                .username("tarasb")
                .build();
        user.setFollowers(Collections.emptyList());
        user.setFollows(Collections.emptyList());

        UserDTO dto = UserMapper.INSTANCE.toDto(user);

        assertEquals(100L, dto.getId());
        assertEquals("Taras", dto.getFirstName());
        assertEquals("Bondarenko", dto.getLastName());
        assertEquals("taras.bondarenko@example.com", dto.getEmail());
        assertEquals("tarasb", dto.getUsername());

        assertEquals(0, dto.getFollowers());
        assertEquals(0, dto.getFollowing());
    }

    @Test
    void testToEntity_BasicMapping() {
        UserDTO dto = UserDTO.builder()
                .id(55L)
                .firstName("Kateryna")
                .lastName("Kovalchuk")
                .email("kateryna.kovalchuk@example.com")
                .username("katyak")
                .followers(5)
                .following(2)
                .build();

        User user = UserMapper.INSTANCE.toEntity(dto);

        assertEquals(55L, user.getId());
        assertEquals("Kateryna", user.getFirstName());
        assertEquals("Kovalchuk", user.getLastName());
        assertEquals("kateryna.kovalchuk@example.com", user.getEmail());
        assertEquals("katyak", user.getUsername());

        assertNull(user.getPassword());
        assertNull(user.getFollowers());
        assertNull(user.getFollows());
        assertNull(user.getVerificationCodes());
        assertNull(user.getRecipes());
        assertNull(user.getNotifications());
        assertFalse(user.isVerified());
    }

    @Test
    void testToEntity_IgnoresFollowersAndFollowingFromDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(99L);
        dto.setFirstName("Yevhen");
        dto.setLastName("Ivanov");
        dto.setEmail("yevhen.ivanov@example.com");
        dto.setUsername("yevheni");
        dto.setFollowers(1000);
        dto.setFollowing(500);

        User user = UserMapper.INSTANCE.toEntity(dto);

        assertEquals(99L, user.getId());
        assertEquals("Yevhen", user.getFirstName());
        assertEquals("Ivanov", user.getLastName());
        assertEquals("yevhen.ivanov@example.com", user.getEmail());
        assertEquals("yevheni", user.getUsername());

        assertNull(user.getFollowers());
        assertNull(user.getFollows());
    }
}
