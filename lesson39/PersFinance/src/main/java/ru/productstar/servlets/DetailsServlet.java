package ru.productstar.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.productstar.servlets.model.Expense;
import ru.productstar.servlets.model.Transaction;

import java.io.IOException;
import java.util.List;

public class DetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var context = req.getServletContext();
        resp.getWriter().println("Transactions: ");
        for (Transaction t : (List<Transaction>)context.getAttribute("transactions")) {
            String type = t.isIncome() ? "Income" : "Expense";
            resp.getWriter().println(String.format("- %s: %s(%d)", type, t.getName(), t.getSum()));
        }
        resp.getWriter().println("\n");
    }
}
