package com.challenge.lendtech.repository;

import com.challenge.lendtech.lendingservice.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
