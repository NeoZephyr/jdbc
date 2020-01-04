package com.pain.flame;

import com.pain.flame.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

@Test
public class TransactionTest {

    public void testV1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("tx_v1.xml");
        UserService userService = (UserService) context.getBean("userServiceProxy");
//        userService.getProfile();
        userService.updateProfile();
    }

    public void testV2() {
        ApplicationContext context = new ClassPathXmlApplicationContext("tx_v2.xml");
        UserService userService = (UserService) context.getBean("userServiceProxy");
        userService.getProfile();
//        userService.updateProfile();
    }

    public void testV3() {
        ApplicationContext context = new ClassPathXmlApplicationContext("tx_v3.xml");
        UserService userService = (UserService) context.getBean("userService");
        userService.getProfile();
//        userService.updateProfile();
    }

    public void testV4() {
        ApplicationContext context = new ClassPathXmlApplicationContext("tx_v4.xml");
        UserService userService = (UserService) context.getBean("userService");
        userService.getProfile();
//        userService.updateProfile();
    }
}
