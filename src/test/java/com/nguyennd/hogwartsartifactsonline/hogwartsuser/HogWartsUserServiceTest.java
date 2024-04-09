package com.nguyennd.hogwartsartifactsonline.hogwartsuser;

import com.nguyennd.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HogWartsUserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<HogWartsUser> hogWartsUsers;

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
    void testFindUserByIdSuccess() {
        // Given
        HogWartsUser u3 = new HogWartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setEnable(false);
        u3.setRoles("user");

        given(this.userRepository.findById(3)).willReturn(Optional.of(u3));

        // When
        HogWartsUser foundHogWartsUser = this.userService.findById(3);

        // Then
        assertThat(foundHogWartsUser.getId()).isEqualTo(3);
        assertThat(foundHogWartsUser.getUsername()).isEqualTo(u3.getUsername());
        assertThat(foundHogWartsUser.isEnable()).isEqualTo(u3.isEnable());
        assertThat(foundHogWartsUser.getRoles()).isEqualTo(u3.getRoles());
        verify(this.userRepository, times(1)).findById(3);
    }

    @Test
    void testFindUserByIdNotFound() {
        // Given
        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.findById(3);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find HogWartsUser with Id - 3 :(");
        verify(this.userRepository, times(1)).findById(3);
    }

    @Test
    void testFindAllUserSuccess() {
        // Given
        given(this.userRepository.findAll()).willReturn(this.hogWartsUsers);

        // When
        List<HogWartsUser> allHogWartsUsers = this.userService.findAll();

        // Then
        assertThat(allHogWartsUsers).hasSameSizeAs(this.hogWartsUsers);
        verify(this.userRepository, times(1)).findAll();

    }

    @Test
    void testSaveUserSuccess() {
        // Given
        HogWartsUser newHogWartsUser = this.hogWartsUsers.get(0);
        newHogWartsUser.setId(9);
        newHogWartsUser.setPassword("Home@123");

        given(this.passwordEncoder.encode(newHogWartsUser.getPassword())).willReturn("Encoded Password");
        given(this.userRepository.save(newHogWartsUser)).willReturn(newHogWartsUser);

        // When
        HogWartsUser savedHogWartsUser = this.userService.save(newHogWartsUser);

        // Then
        assertThat(savedHogWartsUser.getId()).isEqualTo(newHogWartsUser.getId());
        assertThat(savedHogWartsUser.getUsername()).isEqualTo(newHogWartsUser.getUsername());
        assertThat(savedHogWartsUser.getPassword()).isEqualTo(newHogWartsUser.getPassword());
        assertThat(savedHogWartsUser.isEnable()).isEqualTo(newHogWartsUser.isEnable());
        assertThat(savedHogWartsUser.getRoles()).isEqualTo(newHogWartsUser.getRoles());
        verify(this.userRepository, times(1)).save(newHogWartsUser);
    }

    @Test
    void testUpdateUserSuccess() {
        // Given
        HogWartsUser u1 = new HogWartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setEnable(true);
        u1.setRoles("admin user");

        HogWartsUser update = new HogWartsUser();
        update.setUsername("john update");
        update.setEnable(false);
        update.setRoles("user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(u1));
        given(this.userRepository.save(u1)).willReturn(u1);

        // When
        HogWartsUser updatedHogWartsUser = this.userService.update(1, update);

        // Then
        assertThat(updatedHogWartsUser.getId()).isEqualTo(1);
        assertThat(updatedHogWartsUser.getUsername()).isEqualTo(update.getUsername());
        assertThat(updatedHogWartsUser.isEnable()).isEqualTo(update.isEnable());
        assertThat(updatedHogWartsUser.getRoles()).isEqualTo(update.getRoles());
        verify(this.userRepository, times(1)).findById(1);
        verify(this.userRepository, times(1)).save(u1);
    }

    @Test
    void testUpdateUserNotFound() {
        // Given
        HogWartsUser update = new HogWartsUser();
        update.setUsername("john update");
        update.setEnable(false);
        update.setRoles("user");

        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () ->
                userService.update(1, update));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find HogWartsUser with Id - 1 :(");
        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUserSuccess() {
        // Given
        HogWartsUser u1 = new HogWartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setEnable(true);
        u1.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(u1));
        doNothing().when(this.userRepository).deleteById(1);

        // When
        this.userService.delete(1);

        // Then
        verify(this.userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        // Given
        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () ->
                this.userService.delete(99));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find HogWartsUser with Id - 99 :(");
        verify(this.userRepository, times(1)).findById(99);

    }
}
