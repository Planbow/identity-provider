package com.planbow.idp.entities;


import com.planbow.util.data.support.entities.hibernate.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "states")
public class States implements BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="stateId")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="fipsCode")
    private String fipsCode;

    @Column(name="iso2")
    private String iso2;

    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name="modifiedOn")
    private Timestamp modifiedOn;

    @Column(name = "wikiDataId")
    private String wikiDataId;

    @Column(name="isActive")
    private Boolean isActive;

    @Column(name = "countryId")
    private Long countryId;
}
