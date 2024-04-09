package com.nguyennd.hogwartsartifactsonline.hogwartsuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HogWartsUser, Integer> {
    Optional<HogWartsUser> findByUsername(String username);
}
