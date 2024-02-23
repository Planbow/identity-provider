package com.planbow.idp.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planbow.idp.entities.*;
import com.planbow.idp.repository.PublicApiRepository;
import com.planbow.idp.utility.PlanbowUtility;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import com.planbow.util.json.handler.response.ResponseJsonHandler;
import com.planbow.util.json.handler.response.util.ResponseConstants;
import com.planbow.util.json.handler.response.util.ResponseJsonUtil;
import com.planbow.util.utility.core.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
@Log4j2
public class PublicApiService {

    private PublicApiRepository publicApiRepository;
    private Configuration configuration;
    private JavaMailSender javaMailSender;
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Value("${verify.url}")
    private String url;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    public void setPublicApiRepository(PublicApiRepository publicApiRepository) {
        this.publicApiRepository = publicApiRepository;
    }

    public List<Countries> getCountries() {
        return publicApiRepository.getCountries();
    }


    public ResponseJsonHandler getCountry(Long countryId) {
        Countries countries = publicApiRepository.getCountry(countryId);
        if (countries == null)
            return ResponseJsonUtil.getResponse("provided countryId does not exists", 404, ResponseConstants.NOT_FOUND.getStatus(), true);

        return ResponseJsonUtil.getResponse(countries, 200, ResponseConstants.SUCCESS.getStatus(), false);
    }

    public ResponseJsonHandler getStates(Long countryId) {
        List<States> states = publicApiRepository.getStates(countryId);
        return ResponseJsonUtil.getResponse(states, 200, ResponseConstants.SUCCESS.getStatus(), false);
    }

    public ResponseJsonHandler getCities(Long stateId) {
        List<Cities> cities = publicApiRepository.getCities(stateId);
        return ResponseJsonUtil.getResponse(cities, 200, ResponseConstants.SUCCESS.getStatus(), false);
    }


    public ResponseJsonHandler createAccount(String name, String email, String provider, String password, String contactNo,String profilePic) {
        UserEntity userEntity = publicApiRepository.getUserEntity(email);
        if (provider.equals(UserEntity.PROVIDER_GOOGLE)) {
            if (userEntity == null) {
                userEntity = new UserEntity();
                userEntity.setCreatedOn(Utility.getCustomTimestamp());
                userEntity.setName(name);
                userEntity.setEmail(email);
                userEntity.setProfilePic(profilePic);
                userEntity.setProvider(provider);
                userEntity.setContactNo(contactNo);
                userEntity.setIsAccountVerified(true);
                userEntity.setIsActive(true);
                userEntity.setAttempts(0);
            }
            userEntity.setModifiedOn(Utility.getCustomTimestamp());
            Set<RolesEntity> rolesEntities  = userEntity.getRolesEntity();
            if(rolesEntities==null){
                rolesEntities  = new HashSet<>();
                rolesEntities.add(publicApiRepository.getUserRoleEntity());
            }
            userEntity.setRolesEntity(rolesEntities);

        } else if (provider.equals(UserEntity.PROVIDER_PLANBOW)) {
            if (userEntity != null)
                return ResponseJsonUtil.getResponse("Provided email already exists", 400, ResponseConstants.BAD_REQUEST.getStatus(), true);
            userEntity = new UserEntity();

            userEntity.setName(name);
            userEntity.setEmail(email);
            userEntity.setProvider(provider);
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setContactNo(contactNo);
            userEntity.setCreatedOn(Utility.getCustomTimestamp());
            userEntity.setModifiedOn(Utility.getCustomTimestamp());
            userEntity.setIsAccountVerified(false);
            userEntity.setIsActive(false);
            Set<RolesEntity> rolesEntities = new HashSet<>();
            rolesEntities.add(publicApiRepository.getUserRoleEntity());
            userEntity.setRolesEntity(rolesEntities);
        } else
            return ResponseJsonUtil.getResponse("Invalid provider type must be planbow or google only", 400, ResponseConstants.BAD_REQUEST.getStatus(), true);
        userEntity=publicApiRepository.addUserEntity(userEntity);
        UserEntity finalUserEntity = userEntity;
        new Thread(()-> confirmRegistrationEmail(finalUserEntity, provider)).start();
        return ResponseJsonUtil.getResponse(ResponseConstants.SUCCESS.getStatus(), 200, ResponseConstants.SUCCESS.getStatus(), false);

    }


    @Async("asyncExecutor")
    public void confirmRegistrationEmail(UserEntity userEntity, String provider) {
        if (provider.equals(UserEntity.PROVIDER_PLANBOW)) {
            log.info("Sending email confirmation to user");
            String token = UUID.randomUUID().toString();
            createVerificationToken(userEntity, token);
            configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
            String verifyUrl = url + "/verify/" + token;
            Map<String, Object> model = new HashMap<>();
            model.put("name", userEntity.getName());
            model.put("verifyUrl", verifyUrl);
            model.put("date", PlanbowUtility.formatDate(new Date()));
            String content = """
                Please verify your email id by clicking below link
                """;

            model.put("content",content);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            Template t = null;
            try {
                t = configuration.getTemplate("emailVerification.ftl");
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                helper.setTo(userEntity.getEmail());
                helper.setFrom("no-reply@planbow.com");
                helper.setSubject("Planbow - Verify email account");
                helper.setText(text, true);
                javaMailSender.send(message);
            } catch (TemplateException | MessagingException | IOException e) {
                log.error("exception {}",e.getMessage());
            }
        } else {
            log.info("Sending greeting email to user");
        }
    }

    public String verifyToken(String token) {
        TokenVerificationEntity tokenVerificationEntity = publicApiRepository.getTokenVerificationEntity(token);
        if (tokenVerificationEntity != null) {
            UserEntity userEntity = tokenVerificationEntity.getUserEntity();
            if (userEntity != null) {
                if (Boolean.TRUE.equals(userEntity.getIsAccountVerified())) {
                    return "alreadyVerified";
                } else {
                    userEntity.setIsActive(true);
                    userEntity.setIsAccountVerified(true);
                    publicApiRepository.addUserEntity(userEntity);
                    return "verificationSuccess";
                }
            } else return "verificationFailure";

        } else {
            return "verificationFailure";
        }
    }

    public void createVerificationToken(UserEntity userEntity, String token) {
        TokenVerificationEntity tokenVerificationEntity = new TokenVerificationEntity();
        tokenVerificationEntity.setIsActive(true);
        tokenVerificationEntity.setToken(token);
        tokenVerificationEntity.setCreatedOn(Utility.getCustomTimestamp());
        tokenVerificationEntity.setUserEntity(userEntity);
        publicApiRepository.addTokenVerificationEntity(tokenVerificationEntity);
    }

    public ResponseJsonHandler getClients(boolean secretsRequired) {
        ArrayNode arrayNode  = objectMapper.createArrayNode();
        List<Clients> clients  = publicApiRepository.getClients();
        clients.forEach(e->{
            ObjectNode node  = objectMapper.createObjectNode();
            node.put("id",e.getId());
            node.put("clientId",e.getClientId());
            if(secretsRequired)
                node.put("clientSecret",e.getClientName());
            arrayNode.add(node);
        });
        return ResponseJsonUtil.getResponse(arrayNode,200,ResponseConstants.SUCCESS.getStatus(),false);
    }

    public ResponseJsonHandler getClient(String clientId,boolean secretsRequired) {
        Clients clients = publicApiRepository.getClient(clientId);
        if(clients==null)
            return ResponseJsonUtil.getResponse("Provided clientId does not exists",404,ResponseConstants.NOT_FOUND.getStatus(),true);

        ObjectNode node  = objectMapper.createObjectNode();
        node.put("id",clients.getId());
        node.put("clientId",clients.getClientId());
        if(secretsRequired)
            node.put("clientSecret",clients.getClientName());
        return ResponseJsonUtil.getResponse(node,200,ResponseConstants.SUCCESS.getStatus(),false);
    }
}
