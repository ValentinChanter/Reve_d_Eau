package com.keio.marketplace.servlet;

import com.keio.marketplace.controller.CartController;
import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.utils.CartUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "debugAddProductsToCart", value = "/debug")
public class DebugAddProductsToCart extends CartController {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ArticlesDAO articlesDAO = new ArticlesDAO();
        Articles vittel = null;
        try {
            vittel = articlesDAO.getArticle("Bouteille 1L Vittel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            CartUtil.addArticleToCart(req, vittel, 5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.doGet(req, resp);
    }
}
