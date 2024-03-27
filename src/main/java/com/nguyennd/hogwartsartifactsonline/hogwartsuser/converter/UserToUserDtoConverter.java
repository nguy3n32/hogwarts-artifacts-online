package com.nguyennd.hogwartsartifactsonline.hogwartsuser.converter;

import com.nguyennd.hogwartsartifactsonline.hogwartsuser.HogWartsUser;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogWartsUser, UserDto> {
    @Override
    public UserDto convert(HogWartsUser source) {
        return new UserDto(
                source.getId(),
                source.getUsername(),
                source.isEnable(),
                source.getRoles()
        );
    }
}
