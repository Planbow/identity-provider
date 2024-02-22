package com.planbow.idp.repository;



import com.planbow.idp.entities.*;
import com.planbow.idp.utility.PlanbowUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import com.planbow.util.data.support.repository.HibernateRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublicApiRepository extends HibernateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Countries> getCountries() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Countries> countriesCriteriaQuery = criteriaBuilder.createQuery(Countries.class);
        Root<Countries> serviceEntityRoot = countriesCriteriaQuery.from(Countries.class);
        countriesCriteriaQuery.select(serviceEntityRoot).distinct(true);
        return entityManager.createQuery(countriesCriteriaQuery)
                .getResultList();
    }


    public Countries getCountry(Long countryId) {
        return (Countries) getEntity(Countries.class, countryId);
    }


    public List<States> getStates(Long countryId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<States> statesCriteriaQuery = criteriaBuilder.createQuery(States.class);
        Root<States> statesRoot = statesCriteriaQuery.from(States.class);
        statesCriteriaQuery.select(statesRoot).distinct(true);
        statesCriteriaQuery
                .where(
                        criteriaBuilder.equal(statesRoot.get("countryId"), countryId),
                        criteriaBuilder.equal(statesRoot.get(PlanbowUtility.IS_ACTIVE), true)
                );
        return entityManager.createQuery(statesCriteriaQuery)
                .getResultList();

    }

    public List<Cities> getCities(Long stateId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cities> statesCriteriaQuery = criteriaBuilder.createQuery(Cities.class);
        Root<Cities> statesRoot = statesCriteriaQuery.from(Cities.class);
        statesCriteriaQuery.select(statesRoot).distinct(true);
        statesCriteriaQuery
                .where(
                        criteriaBuilder.equal(statesRoot.get("stateId"), stateId),
                        criteriaBuilder.equal(statesRoot.get(PlanbowUtility.IS_ACTIVE), true)
                );
        return entityManager.createQuery(statesCriteriaQuery)
                .getResultList();

    }

    public UserEntity addUserEntity(UserEntity userEntity) {
        saveOrUpdateEntity(userEntity);
        return userEntity;
    }

    public void updateUserEntity(UserEntity userEntity) {
        updateEntity(userEntity);
    }

    public UserEntity getUserEntity(String email, Boolean isActive) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> userEntityCriteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> userEntityRoot = userEntityCriteriaQuery.from(UserEntity.class);
        userEntityCriteriaQuery.select(userEntityRoot).distinct(true);
        userEntityCriteriaQuery
                .where(
                        criteriaBuilder.equal(userEntityRoot.get("email"), email),
                        criteriaBuilder.equal(userEntityRoot.get(PlanbowUtility.IS_ACTIVE), isActive)
                );
        List<UserEntity> userEntities = entityManager.createQuery(userEntityCriteriaQuery)
                .getResultList();
        if (userEntities != null && !userEntities.isEmpty())
            return userEntities.get(0);
        return null;
    }

    public UserEntity getUserEntity(String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> userEntityCriteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> userEntityRoot = userEntityCriteriaQuery.from(UserEntity.class);
        userEntityCriteriaQuery.select(userEntityRoot).distinct(true);
        userEntityCriteriaQuery.where(
                criteriaBuilder.equal(userEntityRoot.get("email"), email));
        List<UserEntity> userEntities = entityManager.createQuery(userEntityCriteriaQuery)
                .getResultList();
        if (userEntities != null && !userEntities.isEmpty()) {
            return userEntities.get(0);
        }
        return null;
    }
    public UserEntity getUserEntity(Long id) {
        return (UserEntity) getEntity(UserEntity.class,id);
    }

    public RolesEntity getAdminRoleEntity() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RolesEntity> userEntityCriteriaQuery = criteriaBuilder.createQuery(RolesEntity.class);
        Root<RolesEntity> userEntityRoot = userEntityCriteriaQuery.from(RolesEntity.class);
        userEntityCriteriaQuery.select(userEntityRoot).distinct(true);
        userEntityCriteriaQuery.where(
                criteriaBuilder.equal(userEntityRoot.get(PlanbowUtility.IS_ACTIVE),true),
                criteriaBuilder.equal(userEntityRoot.get("roleName"),"admin"));
        List<RolesEntity> userEntities = entityManager.createQuery(userEntityCriteriaQuery)
                .getResultList();
        if (userEntities != null && !userEntities.isEmpty())
            return userEntities.get(0);
        return null;
    }

    public RolesEntity getUserRoleEntity() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RolesEntity> userEntityCriteriaQuery = criteriaBuilder.createQuery(RolesEntity.class);
        Root<RolesEntity> userEntityRoot = userEntityCriteriaQuery.from(RolesEntity.class);
        userEntityCriteriaQuery.select(userEntityRoot).distinct(true);
        userEntityCriteriaQuery.where(
                criteriaBuilder.equal(userEntityRoot.get(PlanbowUtility.IS_ACTIVE),true),
                criteriaBuilder.equal(userEntityRoot.get("roleName"),"user"));
        List<RolesEntity> userEntities = entityManager.createQuery(userEntityCriteriaQuery)
                .getResultList();
        if (userEntities != null && !userEntities.isEmpty())
            return userEntities.get(0);
        return null;
    }

    public TokenVerificationEntity addTokenVerificationEntity(TokenVerificationEntity tokenVerificationEntity) {
        //saveOrUpdateEntity(tokenVerificationEntity);
        return tokenVerificationEntity;
    }

    public TokenVerificationEntity getTokenVerificationEntity(String token) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TokenVerificationEntity> tokenVerificationEntityCriteriaQuery = criteriaBuilder.createQuery(TokenVerificationEntity.class);
        Root<TokenVerificationEntity> tokenVerificationEntityRoot = tokenVerificationEntityCriteriaQuery.from(TokenVerificationEntity.class);
        tokenVerificationEntityCriteriaQuery.select(tokenVerificationEntityRoot).distinct(true);
        tokenVerificationEntityCriteriaQuery.where(
                criteriaBuilder.equal(tokenVerificationEntityRoot.get("token"), token),
                criteriaBuilder.equal(tokenVerificationEntityRoot.get(PlanbowUtility.IS_ACTIVE), true)
        );
        List<TokenVerificationEntity> tokenVerificationEntities = entityManager.createQuery(tokenVerificationEntityCriteriaQuery)
                .getResultList();
        if (tokenVerificationEntities != null && !tokenVerificationEntities.isEmpty())
            return tokenVerificationEntities.get(0);

        return null;
    }

    public Clients getClient(String clientId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Clients> clientsCriteriaQuery = criteriaBuilder.createQuery(Clients.class);
        Root<Clients> clientsRoot = clientsCriteriaQuery.from(Clients.class);
        clientsCriteriaQuery.select(clientsRoot).distinct(true);

        clientsCriteriaQuery.where(
                criteriaBuilder.equal(clientsRoot.get("clientId"), clientId)
        );
        List<Clients> clients = entityManager.createQuery(clientsCriteriaQuery)
                .getResultList();

        if (clients != null && !clients.isEmpty())
            return clients.get(0);
        return null;
    }

    public List<Clients> getClients() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Clients> clientsCriteriaQuery = criteriaBuilder.createQuery(Clients.class);
        Root<Clients> clientsRoot = clientsCriteriaQuery.from(Clients.class);
        clientsCriteriaQuery.select(clientsRoot).distinct(true);
        List<Clients> clients = entityManager.createQuery(clientsCriteriaQuery)
                .getResultList();
        return clients;
    }

}
