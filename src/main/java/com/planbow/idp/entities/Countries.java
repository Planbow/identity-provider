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
@Table(name = "countries")
public class Countries implements BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="countryId")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="iso3")
    private String iso3;

    @Column(name="iso2")
    private String iso2;

    @Column(name="dialCode")
    private String dialCode;

    @Column(name="capital")
    private String capital;

    @Column(name="currency")
    private String currency;

    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name="modifiedOn")
    private Timestamp modifiedOn;

    @Column(name = "wikiDataId")
    private String wikiDataId;

    @Column(name="isActive")
    private Boolean isActive;

}
