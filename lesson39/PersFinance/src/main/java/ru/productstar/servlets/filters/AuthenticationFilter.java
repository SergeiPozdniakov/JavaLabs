package ru.productstar.servlets.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Пропускаем без аутентификации страницу логина и статические ресурсы
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if ("/login".equals(path) || path.startsWith("/index.html")) {
            chain.doFilter(request, response);
            return;
        }

        // Проверяем наличие сессии
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            // Сохраняем исходный URL для перенаправления после логина
            String originalUrl = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            if (queryString != null) {
                originalUrl += "?" + queryString;
            }
            httpRequest.getSession(true).setAttribute("originalUrl", originalUrl);
            httpResponse.sendRedirect("/login");
        } else {
            chain.doFilter(request, response);
        }
    }
}