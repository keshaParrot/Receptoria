package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.NotificationDTO;
import github.projectgroup.receptoria.model.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.model.enities.Notification;
import github.projectgroup.receptoria.model.enities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = UserPreviewMapper.class)
public interface NotificationMapper {

    @Mapping(source = "id",              target = "id")
    @Mapping(source = "user.id",         target = "userId")
    @Mapping(source = "target.type",     target = "type")
    @Mapping(source = "target.objectId", target = "relatedObjectId")
    //@Mapping(source = "content",         target = "content")
    @Mapping(source = "createdAt",       target = "createdAt")
    @Mapping(source = "initiators",      target = "initiators")
    NotificationDTO toDto(Notification notification);

}

