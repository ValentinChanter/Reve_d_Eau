package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.utils.CheckIntFloat;
import com.scalar.db.exception.transaction.AbortException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "modifyProductServlet", value = "/modifyProduct-servlet")
public class ModifyProductServlet extends HttpServlet {

    private boolean checkEmpty(String nom, String prix, String stock, String image) {
        if((nom == null || nom.isEmpty()) ||
                (prix == null || prix.isEmpty()) ||
                (stock == null || stock.isEmpty()) ||
                (image == null || image.isEmpty())) {
            return true;
        }
        return false;
    }

    private boolean checkValues(String nom, String prix, String stock, String image) {
        return !checkEmpty(nom, prix, stock, image) && CheckIntFloat.checkInt(stock) && CheckIntFloat.checkInt(prix);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArticlesDAO articlesDAO = new ArticlesDAO();

        String nom = req.getParameter("nom");
        String prix = req.getParameter("prix");
        String stock = req.getParameter("stock");
        String image = req.getParameter("image");
        String id = req.getParameter("id");

        boolean correctValues = checkValues(nom, prix, stock, image);

        if(correctValues) {
            Articles modifiedProduct = new Articles(nom, Integer.parseInt(prix), Integer.parseInt(stock), image);
            modifiedProduct.setId(Long.parseLong(id));
            try {
                articlesDAO.updateArticle(modifiedProduct);
            } catch (AbortException e) {
                throw new RuntimeException(e);
            }
            req.getRequestDispatcher("/WEB-INF/view/productManagement.jsp").forward(req, resp);
        }
        else {
            Articles modifiedProduct;
            try {
                modifiedProduct = articlesDAO.getArticle(Long.parseLong(id));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            req.setAttribute("produit", modifiedProduct);
            req.setAttribute("error", true);
            req.getRequestDispatcher("/WEB-INF/view/modifyProduct.jsp").forward(req, resp);
        }
    }
}
