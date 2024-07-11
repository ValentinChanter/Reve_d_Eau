<%@ page contentType="text/html;charset=UTF-8" import="com.keio.marketplace.dao.ArticlesDAO" %>
<html>
<head>
    <title>Cart</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        function calculateTotal() {
            var items = document.querySelectorAll('[id$="-price"]');
            var overallTotal = 0;

            items.forEach(function(item) {
                overallTotal += parseInt(item.innerHTML.replace("¥", ""));
            });

            document.getElementById('total').innerHTML = "¥" + overallTotal;
        }

        function updatePrice(price, value) {
            const result = price * value;
            return "¥" + result;
        }

        window.onload = calculateTotal;
    </script>
</head>
<body>
<%@ include file="components/header.jsp" %>
<%  ArticlesDAO articlesDAO = new ArticlesDAO(); %>
<div class="flex flex-row justify-center pt-8">
    <div class="w-5/6 p-8">
        <h2 class="text-2xl font-semibold mb-2">Summary of your cart</h2>
        <c:if test="${not empty error}">
            <div class="mb-4 rounded-md bg-red-400 p-2.5 text-center text-sm font-medium text-white">${error}</div>
        </c:if>
        <form action="checkStockPostCart-servlet" method="post">
            <div class="mb-8 flex w-full flex-col">
                <c:choose>
                <c:when test="${empty sessionScope.cart || sessionScope.cart.size() == 0}">
                    <div class="flex flex-col justify-center text-lg font-semibold">Your cart is empty</div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${sessionScope.cart}" var="item">

                        <div class="flex flex-row justify-between mb-2 shadow-md p-4 rounded-md">
                            <div class="flex flex-row">
                                <img class="mr-8 max-h-[100px]" src="${item.key.getImage()}" alt="${item.key.getName()}" />
                                <div class="flex flex-col justify-center text-lg font-semibold">${item.key.getName()}</div>
                             </div>
                            <div class="flex flex-row">
                                <div class="mx-4 flex flex-col justify-center">
                                    <span id="${item.key.getId()}-price" class="mb-2 text-right">¥${item.key.getPrice() * item.value}</span>
                                    <label for="${item.key.getId()}-input"></label>
                                    <input
                                        id="${item.key.getId()}-input"
                                        name="${item.key.getId()}-input"
                                        class="block w-32 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none"
                                        value="${item.value}"
                                        type="number"
                                        step="1"
                                        min="0"
                                        max="${articlesDAO.getArticle(item.key.getId()).getStock()}"
                                        onkeyup="
                                                if(this.value > ${articlesDAO.getArticle(item.key.getId()).getStock()}) this.value = ${articlesDAO.getArticle(item.key.getId()).getStock()};
                                                if(this.value < 0) this.value = 0;
                                                document.getElementById('${item.key.getId()}-price').innerHTML = updatePrice(${item.key.getPrice()}, this.value);
                                                calculateTotal();
                                                "
                                        onchange="
                                                document.getElementById('${item.key.getId()}-price').innerHTML = updatePrice(${item.key.getPrice()}, this.value);
                                                calculateTotal();
                                                "
                                    />
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </div>
            <c:if test="${not empty sessionScope.cart && sessionScope.cart.size() > 0}">
                <div class="flex flex-row justify-between">
                    <div class="mr-4 flex w-48 flex-col rounded-full px-8 py-4 shadow-md">
                        <span class="text-3xl font-bold">Total</span>
                        <span class="text-right text-xl" id="total"></span>
                    </div>
                    <div class="flex flex-col justify-center">
                        <button type="submit" class="text-md rounded-full bg-blue-400 px-5 py-3 text-center font-medium text-white hover:bg-blue-500 focus:outline-none">Proceed to payment</button>
                    </div>
                </div>
            </c:if>
        </form>
    </div>
</div>
</body>
</html>