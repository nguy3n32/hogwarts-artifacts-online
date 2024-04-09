package com.nguyennd.hogwartsartifactsonline.security;

import com.nguyennd.hogwartsartifactsonline.hogwartsuser.HogWartsUser;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.MyUserPrincipal;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JWTProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JWTProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Create User Info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogWartsUser hogWartsUser = principal.hogWartsUser();
        UserDto userDto = this.userToUserDtoConverter.convert(hogWartsUser);

        // Create a JWT
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
