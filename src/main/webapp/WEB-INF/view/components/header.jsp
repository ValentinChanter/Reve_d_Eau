<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<div class="flex h-[100px] w-full flex-row justify-between p-4 shadow-lg">
    <div class="w-80"></div>
    <a href="home">
        <div class="flex w-96 flex-col justify-center text-center">
            <img src="https://i.imgur.com/ut5JvtJ.png" alt="logo">
        </div>
    </a>
    <div class="flex flex-row w-80">
        <form class="mr-4 flex h-[calc(100px-2*1rem)] w-[150px] flex-col justify-center" action="cart" method="post">
            <button class="rounded-full bg-blue-400 px-4 py-3 text-white hover:bg-blue-500 focus:outline-none" type="submit">Cart</button>
        </form>
        <c:if test="${sessionScope.user.getAdmin() eq true}">
            <form class="mr-4 flex h-[calc(100px-2*1rem)] w-[150px] flex-col justify-center" action="productManagement" method="get">
                <button class="rounded-full bg-blue-400 px-4 py-3 text-white hover:bg-blue-500 focus:outline-none" type="submit">Dashboard</button>
            </form>
        </c:if>
        <form class="flex h-[calc(100px-2*1rem)] w-[150px] flex-col justify-center" action=
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    "login"
                </c:when>
                <c:otherwise>
                    "logout-servlet"
                </c:otherwise>
            </c:choose>
        method="post">
            <button class="rounded-full bg-blue-400 px-4 py-3 text-white hover:bg-blue-500 focus:outline-none" type="submit">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        Log in
                    </c:when>
                    <c:otherwise>
                        Log out
                    </c:otherwise>
                </c:choose>
            </button>
        </form>
    </div>
</div>
</body>
</html>