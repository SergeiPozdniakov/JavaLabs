package com.example.spring_beans_demo.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final long instanceId = System.currentTimeMillis();

    public void saveUser(String name) {
        System.out.println("Saving user: " + name + " [Instance: " + instanceId + "]");
    }

    public long getInstanceId() {
        return this.instanceId;
    }
}