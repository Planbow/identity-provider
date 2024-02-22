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
@Table(name = "cities")
public class Cities implements BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="cityId")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "stateId")
    private Long stateId;

    @Column(name = "stateCode")
    private String stateCode;


    @Column(name = "countryId")
    private Long countryId;

    @Column(name = "countryCode")
    private String countryCode;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;


    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name="modifiedOn")
    private Timestamp modifiedOn;

    @Column(name = "wikiDataId")
    private String wikiDataId;

    @Column(name="isActive")
    private Boolean isActive;

}
