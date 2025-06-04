package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.model.enities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserPreviewMapperTest {

    private final UserPreviewMapper mapper = Mappers.getMapper(UserPreviewMapper.class);

    @Test
    void testToDto_AllFieldsPopulated() {
        User user = User.builder()
                .id(123L)
                .firstName("TestFirst")
                .lastName("TestLast")
                .build();

        UserPreviewDTO dto = mapper.toDto(user);

        assertNotNull(dto);
        assertEquals(123L, dto.getId());
        assertEquals("TestFirst", dto.getFirstName());
        assertEquals("TestLast", dto.getLastName());
    }

    @Test
    void testToDto_NullUser() {
        UserPreviewDTO dto = mapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void testToDto_NullFields() {
        User user = User.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .build();

        UserPreviewDTO dto = mapper.toDto(user);

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
    }
}
