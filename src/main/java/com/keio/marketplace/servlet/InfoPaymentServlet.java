package com.keio.marketplace.servlet;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.entity.Users;
import com.keio.marketplace.utils.CartUtil;
import com.keio.marketplace.utils.UsersUtil;
import com.scalar.db.exception.transaction.AbortException;
import com.scalar.db.exception.transaction.TransactionException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "infoPaymentServlet", value = "/infoPayment-servlet")
public class InfoPaymentServlet extends HttpServlet {

    private boolean checkEmpty(String nomCarte, String numeroCarte, String dateExpiration, String codeCarte, Object personnalInformationObject) {
        if((nomCarte == null || nomCarte.isEmpty()) ||
                (numeroCarte == null || numeroCarte.isEmpty()) ||
                (dateExpiration == null || dateExpiration.isEmpty()) ||
                (codeCarte == null || codeCarte.isEmpty()) ||
                (personnalInformationObject == null)) {
            return true;
        }
        return false;
    }

    private boolean checkLuhn(String numeroCarte) {
        String numeroCarteSansEspace = numeroCarte.replaceAll("\\s+", "");
        int somme = 0;
        boolean pair = false;
        for (int i = numeroCarteSansEspace.length() - 1; i >= 0; i--) {
            int chiffre = Integer.parseInt(numeroCarteSansEspace.substring(i, i + 1));
            if (pair) {
                chiffre *= 2;
                if (chiffre > 9) {
                    chiffre -= 9;
                }
            }
            somme += chiffre;
            pair = !pair;
        }
        return (somme % 10 == 0);
    }

    private boolean checkValues(String nomCarte, String numeroCarte, String dateExpiration, String codeCarte, Object personnalInformationObject) {
        return !checkEmpty(nomCarte, numeroCarte, dateExpiration, codeCarte, personnalInformationObject) && checkLuhn(numeroCarte);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nomCarte = req.getParameter("nomCarte");
        String numeroCarte = req.getParameter("numeroCarte");
        String dateExpiration = req.getParameter("dateExpiration");
        String codeCarte = req.getParameter("codeCarte");
        String usePointsString = req.getParameter("usePoints");

        boolean usePoints = false;
        if(usePointsString != null) {
            usePoints = usePointsString.equals("on");
        }

        Object personnalInformationObject = req.getSession().getAttribute("personnalInformation");

        boolean correctValues = checkValues(nomCarte, numeroCarte, dateExpiration, codeCarte, personnalInformationObject);

        if(correctValues) {
            Users user = (Users) req.getSession().getAttribute("user");
            Map<String, String> personnalInformation = (Map<String, String>) personnalInformationObject;
            Map<Articles, Integer> cart = CartUtil.getCart(req);

            for (Map.Entry<Articles, Integer> article : cart.entrySet()) {
                ArticlesDAO articlesDAO = new ArticlesDAO();
                Articles modifiedArticle = article.getKey();
                modifiedArticle.setStock(modifiedArticle.getStock() - article.getValue());
                try {
                    articlesDAO.updateArticle(modifiedArticle);
                } catch (AbortException e) {
                    throw new RuntimeException(e);
                }
            }

            int total = (int) req.getSession().getAttribute("total");
            int loyaltyPoints = 0;
            if (usePoints) {
                loyaltyPoints = user.getLoyaltyPoints();
                total = total/100 - user.getLoyaltyPoints();
                try {
                    UsersUtil.removeLoyaltyPoints(user, user.getLoyaltyPoints());
                } catch (TransactionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    UsersUtil.addLoyaltyPoints(user, (int) (total/100));
                } catch (TransactionException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                CartUtil.emptyCart(req);
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
            req.getSession().removeAttribute("total");
            req.getSession().removeAttribute("personnalInformation");
            req.getSession().removeAttribute("error");

            req.getRequestDispatcher("/WEB-INF/view/confirmationPayment.jsp").forward(req, resp);
        }
        else {
            if (!checkLuhn(numeroCarte)) {
                req.setAttribute("error", "Your card number is invalid");
            } else {
                req.setAttribute("error", "Please fill all the fields.");
            }
            req.getRequestDispatcher("/WEB-INF/view/infopayment.jsp").forward(req, resp);
        }
    }
}
