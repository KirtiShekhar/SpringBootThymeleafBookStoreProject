package com.springboot.thymeleaf.bookstore.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookstore_application_user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id", nullable = false, updatable = false, columnDefinition = "integer")
    private Long userRoleId;
    @Column(name = "user_role_name", nullable = false, updatable = false, columnDefinition = "varchar(100)")
    private String userRoleName;
    @OneToMany(mappedBy = "userRole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Roles> userRoles = new HashSet<>();
}
