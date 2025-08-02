package com.example.spring_beans_demo.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Scope("prototype") // Убрали proxyMode
public class NotificationService {
    private final long id = System.nanoTime(); // Используем nanoTime вместо currentMillis

    public NotificationService() {
        System.out.println("Создан новый NotificationService, id=" + id);
    }

    public long getId() {
        return id;
    }

    public void send(String message) {
        System.out.println("Notification [" + id + "]: " + message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationService)) return false;
        NotificationService that = (NotificationService) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}