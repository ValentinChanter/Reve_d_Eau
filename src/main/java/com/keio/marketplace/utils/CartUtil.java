package com.keio.marketplace.utils;

import com.keio.marketplace.dao.ArticlesDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.entity.Users;
import com.scalar.db.exception.transaction.TransactionException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartUtil {
    /**
     * Get the cart from the session.
     * @param req   The request
     * @return      The cart
     */
    public static Map<Articles, Integer> getCart(HttpServletRequest req) {
        Object cartObject = req.getSession().getAttribute("cart");
        return cartObject == null ? new HashMap<>() : (HashMap<Articles, Integer>) cartObject;
    }

    /**
     * Add an article to the session cart. If the user is logged in, also add the article to the database cart.
     * @param req       The request
     * @param article   The article to add
     * @param quantity  The quantity of the article to add
     */
    public static void addArticleToCart(HttpServletRequest req, Articles article, int quantity) throws Exception {
        Map<Articles, Integer> cart = getCart(req);

        boolean isPresent = false;
        for (Map.Entry<Articles, Integer> entry : cart.entrySet()) {
            if (entry.getKey().getId() == article.getId()) {
                cart.put(entry.getKey(), entry.getValue() + quantity);
                isPresent = true;
                break;
            }
        }

        if (!isPresent) {
            cart.put(article, quantity);
        }
        req.getSession().setAttribute("cart", cart);

        Users users = (Users) req.getSession().getAttribute("user");
        if (users != null) {
            UsersUtil.addArticleToCart(users, article, quantity);
        }
    }

    /**
     * Merge cart from the session and the database.
     * @param req       The request
     * @param users     The user
     */
    public static void mergeCart(HttpServletRequest req, Users users) throws Exception {
        Map<Articles, Integer> sessionCart = getCart(req);
        Map<Articles, Integer> databaseCart = UsersUtil.getCart(users);

        boolean isPresent = false;

        for (Map.Entry<Articles, Integer> entry : sessionCart.entrySet()) {
            for (Map.Entry<Articles, Integer> entry2 : databaseCart.entrySet()) {
                if (entry.getKey().getId() == entry2.getKey().getId()) {
                    databaseCart.put(entry2.getKey(), entry.getValue() + entry2.getValue());
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                databaseCart.put(entry.getKey(), entry.getValue());
            }
        }

        UsersUtil.setCart(users, databaseCart);
        req.getSession().setAttribute("cart", databaseCart);
    }

    public static void emptyCart(HttpServletRequest req) throws TransactionException, IOException {
        req.getSession().removeAttribute("cart");
        Users users = (Users) req.getSession().getAttribute("user");
        if (users != null) {
            UsersUtil.emptyCart(users);
        }
    }

    public static String cartToString(Map<Articles, Integer> cart) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Articles, Integer> entry : cart.entrySet()) {
            sb.append(entry.getKey().getId());
            sb.append(":");
            sb.append(entry.getValue());
            sb.append(",");
        }
        return sb.toString();
    }

    public static Map<Articles, Integer> stringToCart(String cart) throws Exception {
        Map<Articles, Integer> cartMap = new HashMap<>();

        if (cart == null || cart.isEmpty()) {
            return cartMap;
        }

        String[] cartArray = cart.split(",");
        for (String cartItem : cartArray) {
            String[] cartItemArray = cartItem.split(":");
            ArticlesDAO articlesDAO = new ArticlesDAO();
            Articles article = articlesDAO.getArticle(Long.parseLong(cartItemArray[0]));
            int quantity = Integer.parseInt(cartItemArray[1]);
            cartMap.put(article, quantity);
        }
        return cartMap;
    }
}
