package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.scalar.db.exception.transaction.AbortException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "modifyDeleteServlet", value = "/modifydelete-servlet")
public class ModifyDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArticlesDAO articlesDAO = new ArticlesDAO();

        Object boutonModifier = req.getParameter("modifier");
        Object boutonSupprimer = req.getParameter("supprimer");

        String id = req.getParameter("id");
        Articles produit = null;
        try {
            produit = articlesDAO.getArticle(Long.parseLong(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(!(boutonModifier == null)) {
            req.setAttribute("produit", produit);
            req.getRequestDispatcher("/WEB-INF/view/modifyProduct.jsp").forward(req, resp);
        }
        if(!(boutonSupprimer == null)) {
            try {
                articlesDAO.deleteArticle(produit);
            } catch (AbortException e) {
                throw new RuntimeException(e);
            }
            req.getRequestDispatcher("/WEB-INF/view/productManagement.jsp").forward(req, resp);
        }
    }
}
