package com.planbow.idp.entities;

import com.planbow.util.data.support.entities.hibernate.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RolesEntity implements BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="roleId")
    private Long id;

    @Column(name="roleName")
    private String roleName;

    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name="modifiedOn")
    private Timestamp modifiedOn;

    @Column(name="isActive")
    private Boolean isActive;

}
