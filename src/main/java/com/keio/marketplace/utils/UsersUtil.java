package com.keio.marketplace.utils;

import com.keio.marketplace.dao.UsersDAO;
import com.keio.marketplace.entity.Articles;
import com.keio.marketplace.entity.Users;
import com.scalar.db.exception.transaction.TransactionException;

import java.io.IOException;
import java.util.Map;

import static com.keio.marketplace.utils.CartUtil.cartToString;
import static com.keio.marketplace.utils.CartUtil.stringToCart;

public class UsersUtil {
    public static void setCart(Users user, Map<Articles, Integer> cart) throws IOException, TransactionException {
        user.setCart(cart == null ? null : cartToString(cart));
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.updateUser(user);
    }

    public static Map<Articles, Integer> getCart(Users user) throws Exception {
        return stringToCart(user.getCart());
    }

    public static void emptyCart(Users user) throws TransactionException, IOException {
        setCart(user, null);
    }

    /**
     * Add an article to the cart and updates the one in the database. If it already exists, sums the quantities.
     * @param user      The user to add the article to the cart.
     * @param article   The article to add to the cart.
     * @param quantity  The quantity of the article to add to the cart.
     */
    public static void addArticleToCart(Users user, Articles article, int quantity) throws Exception {
        Map<Articles, Integer> cart = getCart(user);

        boolean isPresent = false;
        System.out.println(cart);
        for (Map.Entry<Articles, Integer> entry : cart.entrySet()) {
            System.out.println(entry.getKey().getId() + " " + article.getId());
            if (entry.getKey().getId() == article.getId()) {
                cart.put(entry.getKey(), entry.getValue() + quantity);
                isPresent = true;
                break;
            }
        }
        System.out.println(cart);

        if (!isPresent) {
            cart.put(article, quantity);
        }
        System.out.println(cart);
        setCart(user, cart);
    }

    public static void addLoyaltyPoints(Users user, int points) throws IOException, TransactionException {
        user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.updateUser(user);
    }

    public static void removeLoyaltyPoints(Users user, int points) throws TransactionException, IOException {
        user.setLoyaltyPoints(user.getLoyaltyPoints() - points);
        UsersDAO usersDAO = new UsersDAO();
        usersDAO.updateUser(user);
    }
}
