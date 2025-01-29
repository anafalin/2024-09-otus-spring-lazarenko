package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.UserAppInfoDto;
import ru.otus.hw.models.UserApp;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface UserMapper {

    UserAppInfoDto toUserAppInfoDto(UserApp user);

    List<UserAppInfoDto> toUserAppInfoDtos(List<UserApp> users);
}