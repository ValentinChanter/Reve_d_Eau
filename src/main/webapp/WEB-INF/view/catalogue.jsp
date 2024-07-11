<%@ page import="com.keio.marketplace.dao.ArticlesDAO" %>
<%@ page import="com.keio.marketplace.entity.Articles" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.UUID" %>

<html>
<head>
    <title>Catalogue</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<%
    ArticlesDAO articlesDAO = new ArticlesDAO();
    List<Articles> articles = null;
    try {
        articles = articlesDAO.getAllArticles();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>

<body>
    <%@ include file="components/header.jsp" %>

    <div class="flex flex-col justify-center text-xl font-semibold p-3">Our products</div>
    <% for(Articles product : articles) { %>
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

</body>
</html>
