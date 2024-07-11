<%@ page import="java.util.List" %>
<%@ page import="com.keio.marketplace.entity.Articles" %>
<%@ page import="com.keio.marketplace.dao.ArticlesDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Product management</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<%
    ArticlesDAO articlesDAO = new ArticlesDAO();
    List<Articles> articles = articlesDAO.getAllArticles();
%>
<body>

<%@ include file="components/header.jsp" %>

<h1 class="text-5xl font-extrabold dark:text-black px-8 pt-5">Product management page</h1>

<div class="w-4/5 bg-white shadow flex justify-between items-center py-5">
    <div class="h-full w-full flex items-center">
        <form action="productManagement" method="get">
            <button type="submit" class="ml-2 px-10 bg-gray-300 hover:bg-gray-400 text-white font-bold rounded" disabled>
                Product management
            </button>
        </form>
        <form action="fidelityPointsManagement" method="get">
            <button type="submit" class="ml-2 px-10 bg-blue-500 hover:bg-blue-600 text-white font-bold rounded">
                Loyalty points management
            </button>
        </form>
    </div>
</div>

<br>
<form action="addProduct" method="get" class="px-8">
    <button type="submit" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-4 px-8 text-xl rounded inline-flex items-center">Add a product</button>
</form>

<div class="flex flex-col">
    <div class="overflow-x-auto">
        <div class="inline-block min-w-full py-2 sm:px-6 lg:px-8">
            <div class="overflow-hidden">
                <table class="min-w-full text-left text-sm font-light">

                    <thead class="border-b font-medium dark:border-neutral-500">
                        <tr>
                <%--            <th scope="col" class="px-6 py-4">Image</th>--%>
                            <th scope="col" class="px-6 py-4">Name</th>
                            <th scope="col" class="px-6 py-4">ID</th>
                            <th scope="col" class="px-6 py-4">Price</th>
                            <th scope="col" class="px-6 py-4">Stock</th>
                            <th scope="col" class="px-6 py-4">Modify product</th>
                            <th scope="col" class="px-6 py-4">Remove product</th>
                        </tr>
                    </thead>

                    <%
                        for(Articles a : articles) {
                    %>

                    <form action="modifydelete-servlet" method="post">
                        <tr class="border-b dark:border-neutral-500">
                <%--            <td class="whitespace-nowrap px-6 py-4"> <%= a.getImage() %> </td>--%>
                            <td class="whitespace-nowrap px-6 py-4"> <input name="nom" type="text" value='<%= a.getName() %>' readonly> </td>
                            <td class="whitespace-nowrap px-6 py-4"> <input name="id" type="text" value="<%= a.getId() %>" readonly size="30"> </td>
                            <td class="whitespace-nowrap px-6 py-4"> ¥<%= a.getPrice() %></td>
                            <td class="whitespace-nowrap px-6 py-4"> <%= a.getStock() %></td>
                            <td class="whitespace-nowrap px-6 py-4"><button type="submit" name="modifier" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center">Modify</button></td>
                            <td class="whitespace-nowrap px-6 py-4"><button type="submit" name="supprimer" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center">Remove</button></td>
                        </tr>
                    </form>

                    <%
                        }
                    %>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
