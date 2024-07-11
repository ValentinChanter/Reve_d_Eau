<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty user}">
    <c:redirect url="home" />
</c:if>

<html>
<head>
    <title>Signing up</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
<%@ include file="components/header.jsp" %>

<div class="flex min-h-[calc(100vh-100px)] flex-col justify-center">
    <div class="flex flex-row justify-center">
        <div class="w-1/2 rounded-md p-8 shadow-md">
            <c:if test="${not empty error}">
                <div class="mb-4 rounded-md bg-red-400 p-2.5 text-center text-sm font-medium text-white">${error}</div>
            </c:if>
            <form class="mb-10" action="signup-servlet" method="POST">
                <div class="mb-6">
                    <label for="fullname" class="mb-2 block text-sm font-medium text-gray-900">Full name</label>
                    <input name="fullname" id="fullname" type="text" class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none" required placeholder="Yamada Taro" />
                </div>
                <div class="mb-6">
                    <label for="email" class="mb-2 block text-sm font-medium text-gray-900">Email</label>
                    <input name="email" id="email" type="email" class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none" required placeholder="yamada.taro@company.com" />
                </div>
                <div class="mb-6">
                    <label for="password" class="mb-2 block text-sm font-medium text-gray-900">Password</label>
                    <input name="password" id="password" type="password" class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none" required />
                </div>
                <div class="mb-6">
                    <label for="confirmPassword" class="mb-2 block text-sm font-medium text-gray-900">Password confirmation</label>
                    <input name="confirmPassword" id="confirmPassword" type="password" class="block w-full rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none" required />
                </div>
                <button type="submit" class="w-full rounded-full bg-blue-400 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-blue-500 focus:outline-none">Sign up</button>
            </form>
            <form action="login" method="POST">
                <span class="mb-4 block text-sm font-medium text-gray-900">Already registered?</span>
                <button type="submit" class="w-full rounded-full bg-blue-400 px-5 py-2.5 text-center text-sm font-medium text-white hover:bg-blue-500 focus:outline-none">Log in</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
