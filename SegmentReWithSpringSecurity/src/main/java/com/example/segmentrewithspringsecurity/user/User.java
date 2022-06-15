package com.example.segmentrewithspringsecurity.user;

import com.example.segmentrewithspringsecurity.userRelatedEntity.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login_id", length = 30)
    @NotBlank
    private String loginId;

    @Column(name = "password", length = 80)
    @NotBlank
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    @NotNull
    private Group group;


}
