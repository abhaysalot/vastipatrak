package com.aviyan.vastipatrak.controller;

import com.aviyan.vastipatrak.model.Dashboard;
import com.aviyan.vastipatrak.model.Group;
import com.aviyan.vastipatrak.model.UIResponse;
import com.aviyan.vastipatrak.service.DashboardService;
import com.aviyan.vastipatrak.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/getDashboard", method = RequestMethod.POST)
    public UIResponse getDashboard(Principal principal, @RequestBody Group group) {
        if(loginService.isLoginAssociatedWithProprietor(principal.getName(), group)){
            Dashboard dashboard = dashboardService.getDashboard(group);
            if(Objects.nonNull(dashboard)) {
                return UIResponse.builder()
                        .responseData(dashboard)
                        .build();
            } else {
                return UIResponse.builder()
                        .errorMessage("Dashboard is not available for proprietor " + group.getName() + ".")
                        .build();
            }
        } else {
            return UIResponse.builder()
                    .errorMessage("Login doesn't have access to the proprietor selected." + group.getName())
                    .build();
        }
    }
}
