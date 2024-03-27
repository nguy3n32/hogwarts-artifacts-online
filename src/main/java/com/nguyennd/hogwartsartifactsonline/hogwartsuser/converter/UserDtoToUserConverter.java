package com.nguyennd.hogwartsartifactsonline.hogwartsuser.converter;

import com.nguyennd.hogwartsartifactsonline.hogwartsuser.HogWartsUser;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogWartsUser> {
    @Override
    public HogWartsUser convert(UserDto source) {
        HogWartsUser hogWartsUser = new HogWartsUser();
        hogWartsUser.setEnable(source.enabled());
        hogWartsUser.setUsername(source.username());
        hogWartsUser.setRoles(source.roles());
        return hogWartsUser;
    }
}
