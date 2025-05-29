package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.enities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "following",
            expression = "java(user.getFollows() != null ? user.getFollows().size() : 0)")
    @Mapping(target = "followers",
            expression = "java(user.getFollowers() != null ? user.getFollowers().size() : 0)")
    UserDTO toDto(User user);

    @Mapping(target = "follows", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "verificationCodes", ignore = true)
    @Mapping(target = "recipes", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    User toEntity(UserDTO dto);
}
