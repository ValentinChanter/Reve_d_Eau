package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.utils.CheckIntFloat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "addProductServlet", value = "/addProduct-servlet")
public class AddProductServlet extends HttpServlet {

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
        String nom = req.getParameter("nom");
        String prix = req.getParameter("prix");
        String stock = req.getParameter("stock");
        String image = req.getParameter("image");

        boolean correctValues = checkValues(nom, prix, stock, image);

        req.getSession().removeAttribute("productInformation");

        if(correctValues) {
            // Ajouter produit à la bdd
            ArticlesDAO articlesDAO = new ArticlesDAO();
            // Articles newProduct = new Articles(nom, new BigDecimal(prix), new BigInteger(stock), image);
            try {
                articlesDAO.addArticle(nom, Integer.parseInt(prix), Integer.parseInt(stock), image);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            req.getRequestDispatcher("/WEB-INF/view/productManagement.jsp").forward(req, resp);
        }
        else {
            Map<String, String> productInformation = new HashMap<String, String>()
            {{
                put("nom", nom);
                put("prix", prix);
                put("stock", stock);
                put("image", image);
            }};

            req.setAttribute("error", true);
            req.getSession().setAttribute("productInformation", productInformation);
            req.getRequestDispatcher("/WEB-INF/view/addProduct.jsp").forward(req, resp);
        }
    }
}
