package com.example.jwt_demo.repository;

import com.example.jwt_demo.domain.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUserRepository extends JpaRepository<ApiUser,String> {
}
