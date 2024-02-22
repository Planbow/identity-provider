package com.planbow.idp.repository;

import com.planbow.idp.entities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlanbowIdentityRepository /*extends HibernateRepository*/ {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch user details on the basis of user email
     * @param email
     *          - user email whose details to be fetched
     * @return UserEntity
     *          - Return UserEntity from database table for given user email
     * @author Sumit Chouksey  "sumitchouksey2315@gmail.com"
     */
    public Optional<UserEntity> getUserEntity(String email){
        CriteriaBuilder criteriaBuilder  = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> userEntityCriteriaQuery= criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> userEntityRoot= userEntityCriteriaQuery.from(UserEntity.class);
        userEntityCriteriaQuery.select(userEntityRoot).distinct(true);
        userEntityCriteriaQuery.where(
                criteriaBuilder.equal(userEntityRoot.get("email"),email));
        List<UserEntity> userEntities= entityManager.createQuery( userEntityCriteriaQuery )
                .getResultList();
        if(userEntities!=null && !userEntities.isEmpty()){
                return Optional.of(userEntities.get(0));
        }
        return Optional.empty();
    }

}
