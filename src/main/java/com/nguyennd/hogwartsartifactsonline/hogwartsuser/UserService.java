package com.nguyennd.hogwartsartifactsonline.hogwartsuser;

import com.nguyennd.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HogWartsUser findById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(()->
                new ObjectNotFoundException(HogWartsUser.class.getSimpleName(), userId));
    }

    public List<HogWartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogWartsUser save(HogWartsUser newHogWartsUser) {
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

}
