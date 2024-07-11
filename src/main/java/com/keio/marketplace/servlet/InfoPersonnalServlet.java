package com.keio.marketplace.servlet;

import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.utils.CartUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "infoPersonnalServlet", value = "/infoPersonnal-servlet")
public class InfoPersonnalServlet extends HttpServlet {

    private boolean checkEmpty(String nom, String adresse, String telephone) {
        if((nom == null || nom.isEmpty()) ||
                (adresse == null || adresse.isEmpty()) ||
                (telephone == null || telephone.isEmpty())) {
            return true;
        }
        return false;
    }

    private boolean checkValues(String nom, String adresse, String telephone) {
        return !checkEmpty(nom, adresse, telephone);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nom = req.getParameter("nomComplet");
        String adresse = req.getParameter("adresse");
        String telephone = req.getParameter("telephone");

        boolean correctValues = checkValues(nom, adresse, telephone);

        if(correctValues) {
            Map<Articles, Integer> cart = CartUtil.getCart(req);
            int total = 0;
            for (Map.Entry<Articles, Integer> article : cart.entrySet()) {
                total += article.getKey().getPrice() * article.getValue();
            }
            req.getSession().setAttribute("total", total);

            Map<String, String> personnalInformation = new HashMap<String, String>()
            {{
                put("nomComplet", nom);
                put("adresse", adresse);
                put("telephone", telephone);
            }};

            req.getSession().setAttribute("personnalInformation", personnalInformation);

            req.getRequestDispatcher("/WEB-INF/view/infopayment.jsp").forward(req, resp);
        }
        else {
            req.setAttribute("error", true);
            req.getRequestDispatcher("/WEB-INF/view/infopersonnal.jsp").forward(req, resp);
        }
    }
}
