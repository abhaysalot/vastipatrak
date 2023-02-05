package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.Group;
import com.aviyan.vastipatrak.model.Login;
import com.aviyan.vastipatrak.model.Role;
import com.aviyan.vastipatrak.repository.GroupRepository;
import com.aviyan.vastipatrak.repository.LoginRepository;
import com.aviyan.vastipatrak.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Slf4j
public class LoginService {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PlanService planService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    public boolean authenticateLogin(String loginId, String password){
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        boolean authenticated = loginEntityOptional.filter(login -> passwordEncoder.matches(password, login.getPassword())).isPresent();
        if(authenticated){
            Date now = new Date(System.currentTimeMillis());
            if(Objects.isNull(loginEntityOptional.get().getFirstLoginDate())) {
                loginEntityOptional.get().setFirstLoginDate(now);
            }
            if(Objects.nonNull(loginEntityOptional.get().getLastLoginDate())){
                loginEntityOptional.get().setPreviousLoginDate(loginEntityOptional.get().getLastLoginDate());
            } else {
                loginEntityOptional.get().setPreviousLoginDate(now);
            }
            loginEntityOptional.get().setLastLoginDate(now);
            loginEntityOptional.get().setFailedAttempts(0);
            loginRepository.save(loginEntityOptional.get());
        }
        return authenticated;
    }

    public void updateLoginTime(String loginId){
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        if(loginEntityOptional.isPresent()){
            Date now = new Date(System.currentTimeMillis());
            if(Objects.isNull(loginEntityOptional.get().getFirstLoginDate())) {
                loginEntityOptional.get().setFirstLoginDate(now);
            }
            if(Objects.nonNull(loginEntityOptional.get().getLastLoginDate())){
                loginEntityOptional.get().setPreviousLoginDate(loginEntityOptional.get().getLastLoginDate());
            } else {
                loginEntityOptional.get().setPreviousLoginDate(now);
            }
            loginEntityOptional.get().setLastLoginDate(now);
            loginEntityOptional.get().setFailedAttempts(0);
            loginRepository.save(loginEntityOptional.get());
        }
    }

    public void updateFailedLoginTime(String loginId){
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        if(loginEntityOptional.isPresent()){
            Date now = new Date(System.currentTimeMillis());
            loginEntityOptional.get().setLastFailedLoginDate(now);
            loginEntityOptional.get().setFailedAttempts(loginEntityOptional.get().getFailedAttempts() + 1);
            loginRepository.save(loginEntityOptional.get());
        }
    }

    public Login getLogin(String loginId){
        Login login = new Login();
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        if(loginEntityOptional.isPresent()){
            List<Role> roles = new ArrayList<>();
            loginEntityOptional.get().getRoles().forEach((roleEntity -> {
                if(roleEntity.getGroup().isActive()){
                    Role role = new Role();
                    BeanUtils.copyProperties(roleEntity, role);

                    Group group = new Group();
                    BeanUtils.copyProperties(roleEntity.getGroup(), group);

                    role.setGroup(group);
                    roles.add(role);
                }
            }));

            loginEntityOptional.ifPresent(loginEntity -> BeanUtils.copyProperties(loginEntity, login));
            login.setRoles(roles);
            return login;
        }
        return null;
    }

    public List<Login> getLogins(){
        List<Login> logins = new ArrayList<>();
        loginRepository.findAll().forEach(loginEntity -> {
            Login login = new Login();
            List<Role> roles = new ArrayList<>();
            loginEntity.getRoles().forEach((roleEntity -> {
                Role role = new Role();
                BeanUtils.copyProperties(roleEntity, role);

                Group group = new Group();
                BeanUtils.copyProperties(roleEntity.getGroup(), group);

                role.setGroup(group);
                roles.add(role);
            }));

            BeanUtils.copyProperties(loginEntity, login);
            login.setRoles(roles);

            logins.add(login);
        });
        return logins;
    }

    public boolean isLoginExist(String loginId){
        return loginRepository.findByLoginId(loginId).isPresent();
    }

    public boolean isLoginAssociatedWithProprietor(String loginId, Group searchGroup){
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        return loginEntityOptional.map(value -> value.getRoles().stream().anyMatch(
                role -> role.getGroup().getName().equalsIgnoreCase(searchGroup.getName()))).orElse(false);
    }

    public boolean updateRoleToPrimary(String loginId, Role role){
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntity = loginRepository.findByLoginId(loginId);
        if(loginEntity.isPresent()){
            loginEntity.get().getRoles().forEach(roleEntity -> {
                roleEntity.setPrimaryRole(roleEntity.getGroup().getName().equalsIgnoreCase(role.getGroup().getName()));
            });
            loginRepository.save(loginEntity.get());
            return true;
        }
        return false;
    }

    public boolean updatePassword(String loginId, String newPassword){
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        if(loginEntityOptional.isPresent()){
            loginEntityOptional.get().setPassword(passwordEncoder.encode(newPassword));
            loginRepository.save(loginEntityOptional.get());
            return true;
        }
        return false;
    }

    public Login addLogin(Login login) {
        //Insert new login
        Date now = new Date(System.currentTimeMillis());
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(login.getLoginId());
        if(!loginEntityOptional.isPresent()){
            com.aviyan.vastipatrak.entity.Login loginEntity = new com.aviyan.vastipatrak.entity.Login();
            BeanUtils.copyProperties(login, loginEntity);
            loginEntity.setPassword(passwordEncoder.encode(loginEntity.getPassword()));
            //Set create and modified users and time - modified time will be updated always
            loginEntity.setActive(true);
            loginEntity.setCreatedUser(login.getLoginId());
            loginEntity.setModifiedUser(login.getLoginId());
            loginEntity.setCreatedAt(now);
            loginEntity.setModifiedAt(now);
            com.aviyan.vastipatrak.entity.Login savedLoginEntity = loginRepository.save(loginEntity);

            //Insert roles with proprietor
            List<com.aviyan.vastipatrak.entity.Role> savedRoles = addRolesWithProprietor(login.getRoles(), savedLoginEntity);

            savedLoginEntity.setRoles(savedRoles);
            loginRepository.save(savedLoginEntity);

            Login returnLogin = new Login();
            returnLogin.setLoginId(savedLoginEntity.getLoginId());
            return returnLogin;
        }
        return null;
    }

    public List<com.aviyan.vastipatrak.entity.Role> addRolesWithProprietor(List<Role> roles, com.aviyan.vastipatrak.entity.Login loginEntity) {
        Date now = new Date();
        List<com.aviyan.vastipatrak.entity.Role> savedRoles = new ArrayList<>();
        roles.forEach(role -> {
            com.aviyan.vastipatrak.entity.Group savedGroupEntity = null;
            Optional<com.aviyan.vastipatrak.entity.Group> groupEntityOptional = groupRepository.findById(role.getGroup().getId());
            if(!groupEntityOptional.isPresent()) {
                //Insert proprietor if not exists
                com.aviyan.vastipatrak.entity.Group proprietorEntity = new com.aviyan.vastipatrak.entity.Group();
                BeanUtils.copyProperties(role.getGroup(), proprietorEntity);

                //Set proprietor to Active on creation
                proprietorEntity.setActive(true);

                //From registration always set the plan "Free", start date as today and end date after a year.
                com.aviyan.vastipatrak.entity.Plan planEntity = new com.aviyan.vastipatrak.entity.Plan();
                BeanUtils.copyProperties(planService.getPlan("Free"), planEntity);
                proprietorEntity.setSubscriptionStartDate(now);
                proprietorEntity.setSubscriptionEndDate(Date.from(LocalDate.now().plusYears(1).atStartOfDay().toInstant(ZoneOffset.UTC)));
                proprietorEntity.setSubscriptionPlan(planEntity);

                proprietorEntity.setCreatedUser(loginEntity.getLoginId());
                proprietorEntity.setModifiedUser(loginEntity.getLoginId());
                proprietorEntity.setCreatedAt(now);
                proprietorEntity.setModifiedAt(now);

                savedGroupEntity = groupRepository.save(proprietorEntity);
            }
            //Add role
            com.aviyan.vastipatrak.entity.Role roleEntity = new com.aviyan.vastipatrak.entity.Role();
            BeanUtils.copyProperties(role, roleEntity);
            roleEntity.setActive(true);
            roleEntity.setLogin(loginEntity);
            roleEntity.setGroup(groupEntityOptional.orElse(savedGroupEntity));
            roleEntity.setCreatedUser(loginEntity.getLoginId());
            roleEntity.setModifiedUser(loginEntity.getLoginId());
            roleEntity.setCreatedAt(now);
            roleEntity.setModifiedAt(now);
            com.aviyan.vastipatrak.entity.Role savedRole = roleRepository.save(roleEntity);
            savedRoles.add(savedRole);
        });
        return savedRoles;
    }

    public Login updateLogin(String loginId, Login loginToUpdate) {
        com.aviyan.vastipatrak.entity.Login savedLoginEntity = new com.aviyan.vastipatrak.entity.Login();
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        if (loginEntityOptional.isPresent()) {
            loginEntityOptional.get().setActive(true);
            loginEntityOptional.get().setUserEmail(loginToUpdate.getUserEmail());
            loginEntityOptional.get().setUserFirstName(loginToUpdate.getUserFirstName());
            loginEntityOptional.get().setUserLastName(loginToUpdate.getUserLastName());
            loginEntityOptional.get().setModifiedAt(new Date());
            loginEntityOptional.get().setModifiedUser(loginId);
            savedLoginEntity = loginRepository.save(loginEntityOptional.get());
        }

        Login returnLogin = new Login();
        BeanUtils.copyProperties(savedLoginEntity, returnLogin);
        return returnLogin;
    }

    public boolean disableLogin(Login login) {
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(login.getLoginId());
        if(loginEntityOptional.isPresent()){
            loginEntityOptional.get().setActive(false);
            loginEntityOptional.get().setModifiedUser(login.getLoginId());
            loginEntityOptional.get().setModifiedAt(new Date(System.currentTimeMillis()));

            loginRepository.save(loginEntityOptional.get());
            return true;
        }
        return false;
    }

    public void resetPassword(String loginId) throws MessagingException, UnsupportedEncodingException {
        Optional<com.aviyan.vastipatrak.entity.Login> loginEntityOptional = loginRepository.findByLoginId(loginId);
        if(loginEntityOptional.isPresent()){
            //reset the password

            String tempPass = generateRandomPassword();
            if(updatePassword(loginEntityOptional.get().getLoginId(), tempPass)){
                //and send an email
                mailService.send("aviyan_vastipatrak_app", loginEntityOptional.get().getUserEmail(), "Aviyan - Vastipatrak app - Your new password",
                        "Your new password for Vastipatrak app is - " + tempPass);
            } else {
                log.error("There is an issue while resetting password for login {}.", loginEntityOptional.get().getLoginId());
            }
        } else {
            log.warn("An attempt has been made to reset the password for login {} that does not exist", loginId);
        }
    }

    private String generateRandomPassword(){
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            // generate a random number between 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

}
