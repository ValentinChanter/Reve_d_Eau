package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.UsersDAO;
import com.keio.marketplace.entity.Users;
import com.scalar.db.exception.transaction.TransactionException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "updateFidelityPointsManagementServlet", value = "/updateFidelityPointsManagement-servlet")
public class UpdateFidelityPointsManagementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsersDAO usersDAO = new UsersDAO();

        Set<String> parameterNames = req.getParameterMap().keySet();

        for (String parameterName : parameterNames) {
            long userID = Long.parseLong(parameterName);
            String newLoyaltyPointsString = req.getParameter(parameterName);

            int newLoyaltyPointsValue;
            if(!newLoyaltyPointsString.isEmpty()) {
                newLoyaltyPointsValue = Integer.parseInt(newLoyaltyPointsString);
            }
            else {
                newLoyaltyPointsValue = 0;
            }

            Users user = usersDAO.getUser(userID);
            user.setLoyaltyPoints(newLoyaltyPointsValue);

            try {
                usersDAO.updateUser(user);
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
        }

        req.getRequestDispatcher("/WEB-INF/view/fidelityPointsManagement.jsp").forward(req, resp);
    }
}
