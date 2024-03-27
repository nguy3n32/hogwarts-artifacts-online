package com.nguyennd.hogwartsartifactsonline.hogwartsuser;

import com.nguyennd.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.nguyennd.hogwartsartifactsonline.system.Result;
import com.nguyennd.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class HogWartsUserController {

    private final UserService userService;

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final UserDtoToUserConverter userDtoToUserConverter;

    public HogWartsUserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable int userId) {
        HogWartsUser foundHogWartsUser = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(foundHogWartsUser);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Found One Success",
                userDto
        );
    }

    @GetMapping
    public Result findAllUser() {
        List<HogWartsUser> allHogWartsUsers = this.userService.findAll();
        List<UserDto> userDtos = allHogWartsUsers.stream().map(this.userToUserDtoConverter::convert).toList();
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Find All Success",
                userDtos
        );
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogWartsUser newHogWartsUser) {
        HogWartsUser savedHogWartsUser = this.userService.save(newHogWartsUser);
        UserDto savedUserDto = this.userToUserDtoConverter.convert(savedHogWartsUser);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Add Success",
                savedUserDto
        );
    }

    @PutMapping("/{userId}")
    public Result updateUser(@Valid
                             @RequestBody
                             UserDto update,
                             @PathVariable
                             Integer userId) {
        HogWartsUser updateHogWartsUser = this.userDtoToUserConverter.convert(update);
        HogWartsUser updatedHogWartsUser = this.userService.update(userId, updateHogWartsUser);
        UserDto updatedUserDto = this.userToUserDtoConverter.convert(updatedHogWartsUser);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Update Success",
                updatedUserDto
        );
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        this.userService.delete(userId);
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Delete Success",
                null
        );
    }
}
