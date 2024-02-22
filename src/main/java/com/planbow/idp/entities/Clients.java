package com.planbow.idp.entities;


import com.planbow.util.data.support.entities.hibernate.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "oauth2_registered_client")
public class Clients implements BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private String id;

    @Column(name="client_id")
    private String clientId;

    @Column(name="client_name")
    private String clientName;

}
