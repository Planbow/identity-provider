package com.planbow.idp.entities;

import com.planbow.util.data.support.entities.hibernate.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens")
public class TokenVerificationEntity implements BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "createdOn")
    private Timestamp createdOn;

    @OneToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @Column(name = "isActive")
    private Boolean isActive;

}
