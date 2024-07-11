package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.utils.CartUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static java.lang.Math.abs;


@WebServlet(name = "addToCartServlet", value = "/addToCart-servlet")
public class AddToCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String productId = req.getParameter("productId");
        String productQty = req.getParameter("productQty");
        Map<Articles, Integer> cart = CartUtil.getCart(req);

        long productIdLong = Long.parseLong(productId);
        int qty = Integer.parseInt(productQty);
        ArticlesDAO articlesDAO = new ArticlesDAO();
        Articles product = null;
        try {
            product = articlesDAO.getArticle(productIdLong);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean isPresent = false;
        Articles finalProduct = product;
        for (Map.Entry<Articles, Integer> entry : cart.entrySet()) {
            if (entry.getKey().getId() == product.getId()) {
                isPresent = true;
                finalProduct = entry.getKey();
                break;
            }
        }

        if (isPresent) {
            int qtyInCart = cart.get(finalProduct);
            int toPutInCart = qty + qtyInCart;
            if (finalProduct.getStock() < 0){
                int correctedQty = abs(qtyInCart - finalProduct.getStock());
                try {
                    CartUtil.addArticleToCart(req, finalProduct, correctedQty);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    CartUtil.addArticleToCart(req, finalProduct, qty);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                CartUtil.addArticleToCart(req, product, qty);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        req.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(req, resp);
    }
}
