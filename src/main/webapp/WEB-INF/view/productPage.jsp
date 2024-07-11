<%@ page import="com.keio.marketplace.entity.Articles" %>
<%@ page import="com.keio.marketplace.dao.ArticlesDAO" %>
<%@ page import="com.keio.marketplace.utils.CartUtil" %>
<%@ page import="java.math.BigInteger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    long productId = Long.parseLong(request.getParameter("id"));
    ArticlesDAO articlesDAO = new ArticlesDAO();
    Articles product = null;
    try {
        product = articlesDAO.getArticle(productId);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    int inCartQty = ((Integer)request.getAttribute("qty"));
%>

<html>
<head>
    <title><%= product.getName() %></title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body>
    <%@ include file="components/header.jsp" %>

    <div class="mx-5 my-10 ">
        <div class="w-full flex items-start">
            <div class="w-80"></div>
            <img src="<%= product.getImage() %>" alt="product image" class="mb-2 max-h-96 mx-10">
            <div class="flex flex-col justify-start bg-blue-50 p-6 w-1/3">
                <h2 class="text-xl font-semibold mb-4"><%=product.getName() %></h2>
                <div class="flex flex-row items-center justify-between">
                    <p class="text-l font-bold mt-4">¥<%= product.getPrice() %></p>
                    <div class="w-2/3 flex">
                        <form action="<%= request.getContextPath() %>/addToCart-servlet" method="get" class="w-full flex flex-row justify-between">
                            <input type="hidden" name="productId" value="<%= product.getId() %>">
                            <label for="<%= product.getId() %>-input"></label>
                            <c:choose>
                                <c:when test="${empty sessionScope.cart || sessionScope.cart.size() == 0}">
                                    <input
                                            id="<%= product.getId() %>-input"
                                            name="productQty"
                                            class=" w-1/4 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none"
                                            value="1"
                                            type="number"
                                            step="1"
                                            min="1"
                                            max="<%= product.getStock() %>"
                                            onkeyup="if(this.value > <%= product.getStock() %>) this.value = <%= product.getStock() %>; if(this.value < 1) this.value = 0;"
                                    />
                                </c:when>
                                <c:otherwise>
                                    <input
                                            id="<%= product.getId() %>-input"
                                            name="productQty"
                                            class=" w-1/4 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none"
                                            value="1"
                                            type="number"
                                            step="1"
                                            min="1"
                                            max="<%= product.getStock()%>"
                                            onkeyup="if(this.value > <%= product.getStock() %>) this.value = <%= product.getStock() == 1 ? product.getStock() : 0 %>; if(this.value < 1) this.value = 1;"
                                    />
                                </c:otherwise>
                            </c:choose>
                                <button type="submit" class="mx-3 rounded-full bg-blue-400 px- py-2 text-white p-2 hover:bg-blue-500 focus:outline-none">
                                Add to cart
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>


</body>
</html>
