package com.planbow.idp.entities;

import com.planbow.util.data.support.entities.hibernate.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="userId")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="contactNo")
    private String contactNo;

    @Column(name="provider")
    private String provider;

    @Column(name="password")
    private String password;

    @Column(name="gender")
    private String gender;

    @Column(name="dateOfBirth")
    private String dateOfBirth;

    @Column(name="address")
    private String address;

    @Column(name="profilePic")
    private String profilePic;

    @Column(name="countryCode")
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Countries country;

    @ManyToOne
    @JoinColumn(name = "stateId")
    private States states;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private Cities city;

    @Column(name="latitude")
    private double latitude;

    @Column(name="longitude")
    private double longitude;

    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name="modifiedOn")
    private Timestamp modifiedOn;

    @Column(name="isActive")
    private Boolean isActive;

    @Column(name = "isAccountVerified")
    private Boolean  isAccountVerified;

    @Column(name = "attempts")
    private int attempts;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
            joinColumns={@JoinColumn(name="userId", referencedColumnName="userId")},
            inverseJoinColumns={@JoinColumn(name="roleId", referencedColumnName="roleId")})
    private Set<RolesEntity> rolesEntity;



    public UserEntity(UserEntity userEntity){
        this.id=userEntity.id;
        this.email=userEntity.email;
        this.name=userEntity.name;
        this.contactNo=userEntity.contactNo;
        this.provider=userEntity.provider;
        this.password=userEntity.password;
        this.gender=userEntity.gender;
        this.dateOfBirth=userEntity.dateOfBirth;
        this.address=userEntity.address;
        this.profilePic=userEntity.profilePic;
        this.country=userEntity.country;
        this.states=userEntity.states;
        this.city=userEntity.city;
        this.latitude=userEntity.latitude;
        this.longitude=userEntity.longitude;
        this.createdOn=userEntity.createdOn;
        this.modifiedOn=userEntity.modifiedOn;
        this.isActive=userEntity.isActive;
        this.isAccountVerified=userEntity.isAccountVerified;
        this.attempts=userEntity.attempts;
        this.rolesEntity=userEntity.rolesEntity;
    }

    public static final String PROVIDER_GOOGLE="google";
    public static final String PROVIDER_PLANBOW ="planbow";
}
