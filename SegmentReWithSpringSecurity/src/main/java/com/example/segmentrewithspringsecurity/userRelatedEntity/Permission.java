package com.example.segmentrewithspringsecurity.userRelatedEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(length = 20)
    @NotBlank
    private String name;
}
