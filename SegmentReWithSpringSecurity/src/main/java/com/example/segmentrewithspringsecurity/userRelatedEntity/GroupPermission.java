package com.example.segmentrewithspringsecurity.userRelatedEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "group_permission")
public class GroupPermission {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="group_id")
    @NotNull
    private Group group;

    @ManyToOne(optional = false)
    @JoinColumn(name="permission_id")
    @NotNull
    private Permission permission;
}
