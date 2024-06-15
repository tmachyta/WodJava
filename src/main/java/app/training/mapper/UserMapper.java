package app.training.mapper;

import app.training.config.MapperConfig;
import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.dto.user.UserResponseRoleDto;
import app.training.dto.user.UserUpdateImageRequest;
import app.training.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toModel(UserRegistrationRequest request);

    UserResponseDto toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    User updateImageModel(UserUpdateImageRequest request);

    UserResponseRoleDto toUserRoleResponse(User user);
}
