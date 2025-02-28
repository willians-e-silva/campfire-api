package com.campfire.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.campfire.api.entities.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}