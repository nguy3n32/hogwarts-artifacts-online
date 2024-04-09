package com.nguyennd.hogwartsartifactsonline.hogwartsuser;

import com.nguyennd.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public HogWartsUser findById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(()->
                new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), userId));
    }

    public List<HogWartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogWartsUser save(HogWartsUser newHogWartsUser) {
        newHogWartsUser.setPassword(this.passwordEncoder.encode(newHogWartsUser.getPassword()));
        return this.userRepository.save(newHogWartsUser);
    }

    public HogWartsUser update(Integer userId, HogWartsUser update) {
        return this.userRepository.findById(userId).map(
                oldUser -> {
                    oldUser.setUsername(update.getUsername());
                    oldUser.setEnable(update.isEnable());
                    oldUser.setRoles(update.getRoles());
                    return this.userRepository.save(oldUser);
                }
        ).orElseThrow(() -> new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), userId));

    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), userId)
        );
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)// First, we need to find this user from db
                .map(MyUserPrincipal::new) // if found, wrap the returned user instance in a MyUserPrincipal instance
                .orElseThrow(() -> new UsernameNotFoundException("username "+ username + " is not found")); // Otherwise, throws an exception
    }
}
