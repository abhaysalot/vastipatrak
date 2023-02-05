package com.aviyan.vastipatrak;

import com.aviyan.vastipatrak.constant.IncomeRange;
import com.aviyan.vastipatrak.constant.RoleName;
import com.aviyan.vastipatrak.model.*;
import com.aviyan.vastipatrak.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@SpringBootApplication
@EnableSwagger2
public class VastiPatrakApplication {

    public static void main(String[] args) {
        SpringApplication.run(VastiPatrakApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(LoginService loginService, GroupService groupService, FamilyService familyService, DashboardService dashboardService,
                                 MailService mailService, PlanService planService) {
        return (String[] args) -> {
            //If this is first time and there is no data then insert else do not insert the data.
            if(loginService.getLogins().isEmpty()) {
                addDummyDataOnStartup(loginService, groupService, familyService, dashboardService, mailService, planService);
            }
        };
    }

    private void addDummyDataOnStartup(LoginService loginService, GroupService groupService, FamilyService familyService, DashboardService dashboardService,
                                       MailService mailService, PlanService planService){
        addPlans(planService);

        addDummyData(loginService, groupService, familyService, dashboardService, planService);
    }

    private void addPlans(PlanService planService){
        Plan freePlan = new Plan("Free", 5, 10, 2, false);
        Plan bronzePlan = new Plan("Bronze", 10, 50, 5, false);

        planService.addPlan(freePlan);
        planService.addPlan(bronzePlan);
    }

    private void addDummyData(LoginService loginService, GroupService groupService, FamilyService familyService, DashboardService dashboardService, PlanService planService) {
        addDummyLogins(loginService, planService);

        Group savedGroup1 = groupService.getGroup("Shashan Samrat Tambenagar Jain Sangh");
        Group savedGroup2 = groupService.getGroup("Lok Group Jain Sangh");

        addDummyFamilies(familyService, savedGroup1, savedGroup2);

        Family savedFamily1 = familyService.getFamily(savedGroup1, "Abhay Salot");
        Family savedFamily2 = familyService.getFamily(savedGroup2, "Vaibhav Shah");

        System.out.println(dashboardService.getDashboard(savedGroup1));
    }

    private void addDummyLogins(LoginService loginService, PlanService planService) {
        Login login1 = new Login("Login1", "pass",  "Abhay", "Salot", "abhaysalot@gmail.com");
        login1.setActive(true);

        Group group1 = new Group("Shashan Samrat Tambenagar Jain Sangh", "abc@gmail.com");
        group1.setPhoneNumber("9112322332");
        group1.setAddress("Mumbai, India 400090");
        group1.setActive(true);

        Group group2 = new Group("Lok Group Jain Sangh", "xyz@hotmail.com");
        group2.setPhoneNumber("9112345678");
        group2.setAddress("Delhi, India 123212");
        group2.setActive(true);

        Role role1 = new Role(RoleName.ADMIN);
        role1.setActive(true);
        role1.setLogin(login1);
        role1.setGroup(group1);
        role1.setPrimaryRole(true);

        Role role2 = new Role(RoleName.ADMIN);
        role2.setActive(true);
        role2.setLogin(login1);
        role2.setGroup(group2);

        login1.setRoles(Arrays.asList(role1, role2));

        loginService.addLogin(login1);

        loginService.getLogins().forEach(System.out::println);
    }

    private void addDummyFamilies(FamilyService familyService, Group savedGroup1, Group savedGroup2) {

        Family family1 = new Family("Abhay Salot", "4342532234", "xyz@gmail.com",
                "Mulund West, Mumbai", true);
        family1.setGroup(savedGroup1);
        family1.setNativePlace("Datha");
        family1.setSamaj("Ghoghari");
        family1.setBuildingName("Patil Paradise");
        family1.setIncomeRange(IncomeRange.LESSTHAN3L);
        family1.setPrimaryBusiness("Chemical Supply");
        family1.setSadharmik(true);

        Person person1 = new Person();
        person1.setName("abcd");
        person1.setAge(40);
        person1.setSex("M");
        person1.setEmail("abcd@abcd.com");
        person1.setPhoneNumber("123212");
        person1.setActive(true);
        person1.setFamily(family1);

        Person person2 = new Person();
        person2.setName("xyz");
        person2.setAge(10);
        person2.setSex("F");
        person2.setEmail("abcd@abcd.com");
        person2.setPhoneNumber("123212");
        person2.setActive(true);
        person2.setFamily(family1);

        Monk monk1 = new Monk();
        monk1.setName("Jayratna sri ji");
        monk1.setPreviousName("Jyoti");
        monk1.setAge(40);
        monk1.setSex("M");
        monk1.setEmail("abcd@abcd.com");
        monk1.setPhoneNumber("123212");
        monk1.setActive(true);
        monk1.setFamily(family1);

        family1.setPersons(Arrays.asList(person1,person2));
        family1.setMonks(Arrays.asList(monk1));

        family1.setGroup(savedGroup1);
        familyService.addFamily("Dummy", family1);

        Family family2 = new Family("Vaibhav Shah", "1232332332", "abhaysalot@gmail.com",
                "Boriwali West, Mumbai", true);
        family2.setGroup(savedGroup2);
        family1.setNativePlace("Patan");
        familyService.addFamily("Dummy", family2);

        //update family 1 to generate notification - Do not change the order of this. It should be after family 2 gets added
        family1.setId(1);
        person1.setId(1);
        person2.setId(2);
        monk1.setId(1);
        familyService.updateFamily("Dummy", family1);

        Family family3 = new Family("Harsh Trivedi", "435334232", "harsh@gmail.com",
                "Kandivali West, Mumbai", true);
        family3.setGroup(savedGroup2);
        family3.setNativePlace("Sirohi");
        familyService.addFamily("Dummy", family3);

        Family family4 = new Family("Shashi Vardekar", "954325432384", "abcde@gmail.com",
                "Kandivali West, Mumbai", true);
        family4.setGroup(savedGroup1);
        family4.setNativePlace("Ratnagiri");
        familyService.addFamily("Dummy", family4);

        familyService.getFamilies(savedGroup1).forEach(System.out::println);
        familyService.getFamilies(savedGroup2).forEach(System.out::println);
    }
}
