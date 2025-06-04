package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.ReactionDTO;
import github.projectgroup.receptoria.model.enities.Reaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserPreviewMapper.class })
public interface ReactionMapper {

    @Mapping(source = "id",            target = "id")
    @Mapping(source = "reactionValue", target = "reactionValue")
    @Mapping(source = "content",       target = "content")
    @Mapping(source = "owner",         target = "user")
    ReactionDTO toDto(Reaction reaction);
}
