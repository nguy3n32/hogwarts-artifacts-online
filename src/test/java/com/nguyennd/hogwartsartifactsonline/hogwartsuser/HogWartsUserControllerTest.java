package com.nguyennd.hogwartsartifactsonline.hogwartsuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyennd.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.nguyennd.hogwartsartifactsonline.system.StatusCode;
import com.nguyennd.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class HogWartsUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<HogWartsUser> hogWartsUsers;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp(){
        hogWartsUsers = new ArrayList<>();
        HogWartsUser u1 = new HogWartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setEnable(true);
        u1.setRoles("admin user");
        hogWartsUsers.add(u1);

        HogWartsUser u2 = new HogWartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setEnable(true);
        u2.setRoles("user");
        hogWartsUsers.add(u2);

        HogWartsUser u3 = new HogWartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setEnable(false);
        u3.setRoles("user");
        hogWartsUsers.add(u3);

    }

    @AfterEach
    void tearDown(){}

    @Test
    void testFindById() throws Exception {
        // Given
        given(this.userService.findById(1)).willReturn(this.hogWartsUsers.get(0));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Found One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("john"));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        // Given
        given(this.userService.findById(99))
                .willThrow(new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), 99));

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find HogWartsUser with Id - 99 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testFindAllUserSuccess() throws Exception {
        // Given
        given(this.userService.findAll()).willReturn(this.hogWartsUsers);

        // When and Then
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("john"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("eric"));
    }

    @Test
    void testSaveUserSuccess() throws Exception {
        // Given
        HogWartsUser newHogWartsUser = new HogWartsUser();
        newHogWartsUser.setId(5);
        newHogWartsUser.setPassword("Home@123");
        newHogWartsUser.setUsername("alvin");
        newHogWartsUser.setEnable(true);
        newHogWartsUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(newHogWartsUser);

        given(this.userService.save(Mockito.any(HogWartsUser.class))).willReturn(newHogWartsUser);

        // When and Then
        this.mockMvc.perform(post(this.baseUrl+"/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("alvin"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        // Given
        UserDto userDto = new UserDto(
                null,
                "alvin",
                true,
                "user"
        );

        String json = objectMapper.writeValueAsString(userDto);

        HogWartsUser updatedHogWartsUser = this.hogWartsUsers.get(0);
        updatedHogWartsUser.setUsername(userDto.username());
        updatedHogWartsUser.setEnable(userDto.enabled());
        updatedHogWartsUser.setRoles(userDto.roles());

        given(this.userService.update(eq(1), Mockito.any(HogWartsUser.class))).willReturn(updatedHogWartsUser);

        // When and Then
        this.mockMvc.perform(put(this.baseUrl + "/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("alvin"))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        //Given
        UserDto userDto = new UserDto(
                null,
                "alvin",
                true,
                "user"
        );

        String json = this.objectMapper.writeValueAsString(userDto);

        given(this.userService.update(eq(1), Mockito.any(HogWartsUser.class))).willThrow(
                new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), 1)
        );

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.flag").value(false))
                        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                        .andExpect(jsonPath("$.message").value("Could not find HogWartsUser with Id - 1 :("))
                        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        // Given
        doNothing().when(this.userService).delete(2);

        // When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/users/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteUserErrorWithNonExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), 2))
                .when(this.userService).delete(2);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/users/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find HogWartsUser with Id - 2 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}
