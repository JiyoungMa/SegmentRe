package com.example.segmentrewithspringsecurity.userRelatedEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(length = 20)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "group")
    private List<GroupPermission> permissions = new ArrayList<>();
}
