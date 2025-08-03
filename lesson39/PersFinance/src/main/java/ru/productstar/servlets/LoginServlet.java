package ru.productstar.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final String passwordCred = "123456";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Перенаправляем GET-запросы на страницу с формой логина
        req.getRequestDispatcher("/index.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String password = req.getParameter("password");
        if (passwordCred.equals(password)) {
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(60);
            // Сохраняем исходный URL для перенаправления после логина
            String redirectUrl = (String) req.getSession().getAttribute("originalUrl");
            if (redirectUrl == null || redirectUrl.isEmpty()) {
                redirectUrl = "/summary";
            }
            req.getSession().removeAttribute("originalUrl");
            resp.sendRedirect(redirectUrl);
        } else {
            resp.getWriter().println("Wrong password");
        }
    }
}
