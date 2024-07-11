<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.lang.Math, java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Payment information</title>
    <script src="https://cdn.tailwindcss.com"></script>

</head>

<%
    boolean error = false;
    Object errorObject = request.getAttribute("error");
    request.removeAttribute("error");

    if(errorObject != null) {
        error = (boolean) errorObject;
    }
%>

<body>
<%@ include file="components/header.jsp" %>

<div class="flex">
    <div class="flex-1 w-80"></div>
    <div class="flex-1 grow">
        <h1 class="text-4xl font-extrabold dark:text-black pt-5 pb-8">Payment information</h1>

        <c:if test="${not empty error}">
            <p class="text-red-600 text-l font-extrabold py-2">${error}</p>
        </c:if>

        <form action="infoPayment-servlet" method="post" class="pt-8">
            <div class="relative z-0 w-full mb-6 group">
                <input type="text" name="nomCarte" id="nomCarte" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required />
                <label for="nomCarte" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Cardholder name</label>
            </div>
            <div class="relative z-0 w-full mb-6 group">
                <input type="text" pattern="[0-9\s]{4} ?[0-9\s]{4} ?[0-9\s]{4} ?[0-9\s]{4}" name="numeroCarte" id="numeroCarte" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required />
                <label for="numeroCarte" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Card number (**** **** **** ****)</label>
            </div>
            <div class="grid md:grid-cols-2 md:gap-6">
                <div class="relative z-0 w-full mb-6 group">
                    <input type="month" name="dateExpiration" id="dateExpiration" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required />
                    <label for="dateExpiration" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Expiration Date (MM/AA)</label>
                </div>
                <div class="relative z-0 w-full mb-6 group">
                    <input type="text" pattern="[0-9]{3}" name="codeCarte" id="codeCarte" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required />
                    <label for="codeCarte" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Security code (CVC)</label>
                </div>
            </div>
            <div class="mb-6">
                <input id="usePoints" name="usePoints" type="checkbox" onchange="function updateTotal() {
                    let total = ${sessionScope.get("total")};
                    let awardedLoyaltyPoints = Math.floor(total) / 100;
                    let discount = ${Math.round(sessionScope.get("user").getLoyaltyPoints().doubleValue())};

                    if (document.getElementById('usePoints').checked) total -= discount;
                    document.getElementById('total').innerHTML = 'Total: ¥' + total;

                    if (Math.floor(total) < Math.floor(${sessionScope.get("total")})) awardedLoyaltyPoints = Math.floor(total) / 100;
                    document.getElementById('awardedLoyaltyPoints').innerHTML = 'This purchase will earn you ' + awardedLoyaltyPoints + ` loyalty point\${awardedLoyaltyPoints <= 1 ? '' : 's'}`;
                }
                updateTotal()" />
                <label for="usePoints" class="text-sm">Use your ${sessionScope.get("user").getLoyaltyPoints()} loyalty points (- ¥${String.format("%d", Math.round(sessionScope.get("user").getLoyaltyPoints().doubleValue()))})</label>
            </div>
            <div class="mb-6">
                <p class="text-2xl font-semibold" id="total">Total: ¥${sessionScope.get("total")}</p>
<%--                TODO: adapt loyalty points for yen?--%>
                <p class="text-sm text-gray-500" id="awardedLoyaltyPoints">This purchase will earn you ${String.format("%.0f", sessionScope.get("total")/100)} loyalty points</p>
            </div>
            <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Validate</button>
        </form>
    </div>
    <div class="flex-1 w-80"></div>
</div>

</body>
</html>
