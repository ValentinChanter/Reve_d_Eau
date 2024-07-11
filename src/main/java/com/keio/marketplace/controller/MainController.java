package com.keio.marketplace.controller;

import com.keio.marketplace.dao.ArticlesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "mainController", value = "/home")
public class MainController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // If the local Cassandra database is empty, populate it
        ArticlesDAO articlesDAO = new ArticlesDAO();
        try {
            if (articlesDAO.getAllArticles().isEmpty()) {
                articlesDAO.addArticle(17203334644248L, "1L Bottle Jouvence de Wattwiller", 865, 128, "https://i.imgur.com/x4ehCh8.png");
                articlesDAO.addArticle(17203334644240L, "1.15L Bottle Vichy-Célestins", 423, 450, "https://i.imgur.com/EzSPHn9.png");
                articlesDAO.addArticle(17203334644241L, "1L Bottle Châteauneuf-Auverge", 661, 56, "https://i.imgur.com/tSRd01I.png");
                articlesDAO.addArticle(17203334644244L, "1.5L Bottle Cristaline", 185, 1580, "https://i.imgur.com/5SJC8P0.png");
                articlesDAO.addArticle(17203334644250L, "1.5L Bottle Aquarel", 270, 215, "https://i.imgur.com/bKBHHTJ.png");
                articlesDAO.addArticle(17203334644247L, "1L Bottle Celtic", 390, 326, "https://i.imgur.com/Tgxrjft.png");
                articlesDAO.addArticle(17203334644251L, "1.5L Bottle Contrex", 194, 448, "https://i.imgur.com/N7DbSwP.png");
                articlesDAO.addArticle(17203334644252L, "1L Bottle Eau Royale", 1100, 52, "https://i.imgur.com/XJdDBUL.png");
                articlesDAO.addArticle(17203334644253L, "1L Bottle Evian", 268, 1288, "https://i.imgur.com/fw6SEme.png");
                articlesDAO.addArticle(17203334644243L, "1L Bottle Hépar", 405, 812, "https://i.imgur.com/S69xD4Z.png");
                articlesDAO.addArticle(17203334644249L, "1.5L Bottle Jolival", 177, 402, "https://i.imgur.com/SWfyEIB.png");
                articlesDAO.addArticle(17203334644246L, "1L Bottle Mont Roucous", 236, 1058, "https://i.imgur.com/QkURBhi.png");
                articlesDAO.addArticle(17203334644254L, "1L Bottle Rozana", 440, 774, "https://i.imgur.com/GY8B0Q9.png");
                articlesDAO.addArticle(17203334644255L, "1.5L Bottle Saint Amand", 228, 560, "https://i.imgur.com/CkHrhxY.png");
                articlesDAO.addArticle(17203334644242L, "1L Bottle Saint Antonin", 245, 210, "https://i.imgur.com/0FwpMbO.png");
                articlesDAO.addArticle(17203334644256L, "1.5L Bottle Courmayeur", 183, 540, "https://i.imgur.com/jMmNZNV.png");
                articlesDAO.addArticle(17203334644257L, "1.15L Bottle St-Yorre", 218, 798, "https://i.imgur.com/cAiFqA8.png");
                articlesDAO.addArticle(17203334644258L, "1.5L Bottle Thonon", 185, 1185, "https://i.imgur.com/YsRToKO.png");
                articlesDAO.addArticle(17203334644259L, "1L Bottle Vittel", 176, 1454, "https://i.imgur.com/91WeJH8.png");
                articlesDAO.addArticle(17203334644260L, "1.5L Bottle Volvic", 270, 1332, "https://i.imgur.com/0R0HtJu.png");
                articlesDAO.addArticle(17203334644261L, "1L Bottle Suntory", 170, 5820, "https://i.imgur.com/rhgctQk.jpeg");
                articlesDAO.addArticle(17203334644262L, "1.02L Bottle Ilohas", 155, 4205, "https://i.imgur.com/8MdOq9n.png");
                articlesDAO.addArticle(17203334644263L, "2L Bottle FamilyMart", 110, 9728, "https://i.imgur.com/XCpHJFa.png");
                articlesDAO.addArticle(17203334644264L, "2L Bottle 7 Premium", 140, 4558, "https://i.imgur.com/yXAxcSh.png");
                articlesDAO.addArticle(17203334644265L, "0.9L Bottle Pocari Sweat", 171, 2691, "https://i.imgur.com/PuSAjvF.png");
                articlesDAO.addArticle(17203334644266L, "2L Bottle Asahi", 179, 3694, "https://i.imgur.com/Bw6S6xg.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(req, resp);
    }
}
