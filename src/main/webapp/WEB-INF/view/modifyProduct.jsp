<%@ page import="com.keio.marketplace.entity.Articles" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modify product</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<%
    boolean error = false;
    Object errorObject = request.getAttribute("error");
    request.removeAttribute("error");

    if(errorObject != null) {
        error = (boolean) errorObject;
    }

    Object produitObjet = request.getAttribute("produit");
    Articles produit = null;

    if(produitObjet != null) {
        produit = (Articles) produitObjet;
    }
%>
<body>

<%@ include file="components/header.jsp" %>

<h1 class="text-5xl font-extrabold dark:text-black px-8 pt-5">Modify product</h1>

<form action="productManagement" method="get" class="px-8 pt-5">
    <button type="submit" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center">
        Return to the product management page
    </button>
</form>

<%
    if(error) {
%>

<p class="text-red-600 text-xl font-extrabold px-8 py-2">There is one or more error(s) in entering product information.</p>

<%
    }
%>

<div class="w-full max-w-md">
    <form method="post" action="modifyProduct-servlet" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">Product name:</label>
        <input type="text" name="nom" value="<%= produit.getName() %>" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        <br><br>

        <label class="block text-gray-700 text-sm font-bold mb-2">Product price:</label>
        <input type="text" name="prix" value="<%= produit.getPrice() %>" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        <br><br>

        <label class="block text-gray-700 text-sm font-bold mb-2">Product stock:</label>
        <input type="text" name="stock" value="<%= produit.getStock() %>" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        <br><br>

        <label class="block text-gray-700 text-sm font-bold mb-2">Product picture:</label>
        <input type="text" name="image" value="<%= produit.getImage() %>" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
        <br><br>

        <label class="block text-gray-700 text-sm font-bold mb-2">Product ID:</label>
        <input type="text" name="id" value="<%= produit.getId() %>" readonly class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline bg-slate-400">
        <br><br>

        <button type="submit" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center">
            Modify
        </button>
    </form>
</div>
</body>
</html>
