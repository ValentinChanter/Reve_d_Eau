package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.UsersDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.entity.Users;
import com.keio.marketplace.utils.CartUtil;
import com.keio.marketplace.utils.UsersUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "authServlet", value = "/auth-servlet")
public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsersDAO usersDAO = new UsersDAO();
        if (usersDAO.login(req.getParameter("email"), req.getParameter("password"))) {
            Users users = usersDAO.getUser(req.getParameter("email"));
            assert users != null;

            req.getSession().setAttribute("user", users);

            Map<Articles, Integer> cart = CartUtil.getCart(req);
            try {
                if (!cart.isEmpty() && !UsersUtil.getCart(users).isEmpty()) {
                    try {
                        CartUtil.mergeCart(req, users);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if (!cart.isEmpty()) {
                    UsersUtil.setCart(users, cart);
                } else if (!UsersUtil.getCart(users).isEmpty()) {
                    req.getSession().setAttribute("cart", UsersUtil.getCart(users));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            resp.sendRedirect(getServletContext().getContextPath() + "/home");
        } else {
            req.setAttribute("error", "Incorrect email or password. Please try again.");
            req.setAttribute("email", req.getParameter("email"));
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        }
    }
}
