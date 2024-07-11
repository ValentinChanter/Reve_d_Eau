package com.keio.marketplace.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "infoPersonnalController", value = "/infopersonnal")
public class InfoPersonnalController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Object user = req.getSession().getAttribute("user");
        if (user == null) {
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/view/infopersonnal.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Object user = req.getSession().getAttribute("user");
        if (user == null) {
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/view/infopersonnal.jsp").forward(req, resp);
        }
    }
}
