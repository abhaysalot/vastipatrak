package com.aviyan.vastipatrak.service;

import com.aviyan.vastipatrak.model.LoggedInUser;
import com.aviyan.vastipatrak.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class LoggedInUserImpl implements UserDetailsService {
    private static final long serialVersionUID = 1L;

    @Autowired
    LoginService loginService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        LoggedInUser loggedInUser = null;
        Login login = loginService.getLogin(loginId);
        if(Objects.nonNull(login)){
            loggedInUser  = LoggedInUser.builder().username(login.getLoginId()).build();
        } else {
            new UsernameNotFoundException("Login not found with login id: " + loginId);
        }

        return loggedInUser;
    }
}
