package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.entity.Users;
import com.keio.marketplace.utils.CartUtil;
import com.keio.marketplace.utils.UsersUtil;
import com.scalar.db.exception.transaction.TransactionException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "checkStockPostCartServlet", value = "/checkStockPostCart-servlet")
public class CheckStockPostCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<String> parameterNames = req.getParameterMap().keySet();
        Users users = (Users) req.getSession().getAttribute("user");
        Map<Articles, Integer> cart = CartUtil.getCart(req);

        for (String parameterName : parameterNames) {
            String value = req.getParameter(parameterName);
            String name = parameterName.substring(0, parameterName.indexOf("-input"));
            ArticlesDAO articlesDAO = new ArticlesDAO();
            Articles article = null;
            try {
                article = articlesDAO.getArticle(Long.parseLong(name));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (Integer.parseInt(value) == 0) {
                cart.remove(article);
                if (cart.isEmpty()) {
                    if (users != null) {
                        try {
                            UsersUtil.setCart(users, null);
                        } catch (TransactionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    req.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(req, resp);
                    return;
                }

                continue;
            }

            try {
                if (!articlesDAO.checkStock(name, Integer.parseInt(value))) {
                    req.setAttribute("error", "A stock error occured. Please try again.");
                    req.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(req, resp);
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            for (Map.Entry<Articles, Integer> entry : cart.entrySet()) {
                if (entry.getKey().equals(article)) {
                    entry.setValue(Integer.parseInt(value));
                }
            }
        }

        if (users != null) {
            try {
                UsersUtil.setCart(users, cart);
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
        }

        resp.sendRedirect(getServletContext().getContextPath() + "/infopersonnal");
    }
}
