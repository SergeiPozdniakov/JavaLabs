package ru.productstar.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/error", loadOnStartup = 1)
public class ErrorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        Throwable throwable = (Throwable) req.getAttribute("jakarta.servlet.error.exception");
        String message = (String) req.getAttribute("jakarta.servlet.error.message");

        if (statusCode == null) {
            statusCode = 500;
        }

        resp.setStatus(statusCode);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        if (statusCode == 404) {
            writer.println("Error (404) — page not found");
        } else {
            String errorMsg = throwable != null ? throwable.toString() :
                    (message != null ? message : "Unknown error");
            writer.println("Error (" + statusCode + ") — " + errorMsg);
        }
    }
}
