package ru.productstar.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.productstar.servlets.model.Expense;
import ru.productstar.servlets.model.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpensesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var context = req.getServletContext();
        int freeMoney = (int)context.getAttribute("freeMoney");
        var transactions = new ArrayList<Transaction>((List)context.getAttribute("transactions"));

        for (var k : req.getParameterMap().keySet()) {
            int value = Integer.parseInt(req.getParameter(k));
            freeMoney -= value;
            transactions.add(new Transaction(k, value, false));
        }

        context.setAttribute("transactions", transactions);
        context.setAttribute("freeMoney", freeMoney);
        resp.sendRedirect("/summary");
    }
}
