package com.co.eatupapi.repositories.user;

import com.co.eatupapi.domain.user.UserDomain;
import com.co.eatupapi.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserDomain, UUID> {
    boolean existsByEmail(String email);
    Optional<UserDomain> findByEmail(String email);
    List<UserDomain> findByStatus(UserStatus status);
}
