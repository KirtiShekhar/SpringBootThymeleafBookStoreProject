package com.springboot.thymeleaf.bookstore.project.repository;

import com.springboot.thymeleaf.bookstore.project.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByUserRoleName(String userRoleName);
}