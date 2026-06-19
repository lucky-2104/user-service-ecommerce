package com.ecommerce.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.user_service.entity.User;

public interface UserRepository extends JpaRepository<User,UUID> {
	
	
	Optional<User> findByEmail(String email);

}
