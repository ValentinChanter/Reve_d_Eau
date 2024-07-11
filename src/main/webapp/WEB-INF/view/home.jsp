<%@ page import="com.keio.marketplace.entity.Articles" %>
<%@ page import="com.keio.marketplace.dao.ArticlesDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.keio.marketplace.dao.ArticlesDAO" %>
<%@ page import="com.scalar.db.exception.transaction.TransactionException" %>
<%@ page import="com.scalar.db.io.Key" %>
<%@ page import="java.util.Optional" %>
<%@ page import="com.scalar.db.api.*" %>
<%@ page import="com.keio.marketplace.dao.ArticlesDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        .gradient-radial {
            background: radial-gradient(circle, lightskyblue, #60a5fa);
        }
    </style>
</head>

<%
    ArticlesDAO articlesDAO = new ArticlesDAO();
    List<Articles> articles = null;
    Articles bestSeller = null;
    try {
        articles = articlesDAO.getAllArticles();
        bestSeller = articlesDAO.getArticle("1L Bottle Eau Royale");
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>

<body>
    <%@ include file="components/header.jsp" %>

    <div class="flex flex-col pt-8 justify-center">
        <div class="flex justify-center text-center">
            <div class="gradient-radial bg-contain bg-center w-5/6 p-8">
                <h1 class="text-3xl font-semibold mb-2 text-white"> Welcome to Reve d'Eau</h1>
                <p class="text-l font-semibold text-white"> Rediscover the taste of water with our bestseller</p>
                <div class="flex justify-center">
                    <a href="productPage?id=<%= bestSeller.getId() %>">
                        <div class="bg-white float-left   border p-4 m-4 cursor-pointer">
                            <img src="<%= bestSeller.getImage() %>" alt="product image" class="mx-auto mb-2 max-h-32">
                            <div>
                                <h3 class="text-center mb-2"><%= bestSeller.getName() %></h3>
                                <p class="text-center">¥<%= bestSeller.getPrice() %></p>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
        </div >
        <div class="text-center">
            <div class="text-xl pt-4 pb-1">More products</div>
            <div class="flex justify-center">
                <%
                    for (int i=0; i<5; i++) {
                        Articles product = articles.get(i);
                %>
                        <a href="productPage?id=<%= product.getId() %>">
                            <div class=" float-left w-64 h-64 border p-4 m-4 cursor-pointer">
                                <img src="<%= product.getImage() %>" alt="product image" class="mx-auto mb-2 max-h-32">
                                <div>
                                    <h3 class="text-center mb-2"><%= product.getName() %></h3>
                                    <p class="text-center">¥<%= product.getPrice() %></p>
                                </div>
                            </div>
                        </a>
                <% } %>
            </div>
            <div class="p-2">
                <form action="catalogue" method="get">
                    <button class="rounded-full bg-blue-400 px-4 py-3 text-white hover:bg-blue-500 focus:outline-none" type="submit">
                        Discover our full catalogue
                    </button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
