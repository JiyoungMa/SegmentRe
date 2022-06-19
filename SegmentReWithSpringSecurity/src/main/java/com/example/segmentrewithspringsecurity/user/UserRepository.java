package com.example.segmentrewithspringsecurity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("select u from Users u join fetch u.group g left join fetch g.permissions gp join fetch gp.permission where u.loginId = :loginId")
    public Optional<Users> findByLoginId(String loginId);
}
