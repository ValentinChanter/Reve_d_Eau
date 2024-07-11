package com.keio.marketplace.controller;

import com.keio.marketplace.utils.CartUtil;
import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "productPageController", value = "/productPage")
public class ProductPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String productId = req.getParameter("id");
        Map<Articles, Integer> cart = CartUtil.getCart(req);

        if (productId != null && !productId.isEmpty()) {
            ArticlesDAO articlesDAO = new ArticlesDAO();
            Articles product = null;
            try {
                product = articlesDAO.getArticle(Long.parseLong(productId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Articles finalProduct = product;
            for (Map.Entry<Articles, Integer> entry : cart.entrySet()) {
                if (entry.getKey().getId() == product.getId()) {
                    finalProduct = entry.getKey();
                    break;
                }
            }

            if (cart.containsKey(finalProduct)) {
                int qtyInCart = cart.get(finalProduct);
                req.setAttribute("qty", qtyInCart);
            } else {
                req.setAttribute("qty", 0);
            }

            req.setAttribute("product", product);
            req.getRequestDispatcher("/WEB-INF/view/productPage.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/view/catalogue.jsp").forward(req, resp);
        }
    }
}
