package com.campfire.api.repository;

import org.springframework.data.repository.CrudRepository;
import com.campfire.api.entities.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}