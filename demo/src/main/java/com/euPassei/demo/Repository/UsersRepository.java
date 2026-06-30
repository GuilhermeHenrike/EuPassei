package com.euPassei.demo.Repository;

import com.euPassei.demo.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
    boolean existsByUsername(String email);

}
