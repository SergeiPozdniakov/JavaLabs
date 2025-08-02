package com.example.spring_beans_demo;

import com.example.spring_beans_demo.service.NotificationService;
import com.example.spring_beans_demo.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBeansDemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				SpringApplication.run(SpringBeansDemoApplication.class, args);

		testBeanScopes(context); // Вызываем метод проверки
	}

	private static void testBeanScopes(ConfigurableApplicationContext context) {
		System.out.println("\n=== Проверка бинов ===");

		// UserService - singleton
		UserService userService1 = context.getBean(UserService.class);
		UserService userService2 = context.getBean(UserService.class);
		System.out.println("UserService singleton: " + (userService1 == userService2));
		System.out.println("UserService1 ID: " + userService1.getInstanceId());
		System.out.println("UserService2 ID: " + userService2.getInstanceId());

		// NotificationService - prototype
		NotificationService ns1 = context.getBean(NotificationService.class);
		NotificationService ns2 = context.getBean(NotificationService.class);

		// Сравниваем через equals() и hashCode()
		System.out.println("NotificationService prototype: " +
				!ns1.equals(ns2));
		System.out.println("NotificationService1 ID: " + ns1.getId());
		System.out.println("NotificationService2 ID: " + ns2.getId());
		System.out.println("HashCode ns1: " + ns1.hashCode());
		System.out.println("HashCode ns2: " + ns2.hashCode());
	}
}