package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.model.enities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPreviewMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    UserPreviewDTO toDto(User user);
}
