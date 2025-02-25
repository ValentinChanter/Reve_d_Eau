<%@ page import="com.keio.marketplace.entity.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Personal information</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<%
    Object userObject = request.getSession().getAttribute("user");
    Users user = new Users();

    if(userObject != null) {
        user = (Users) userObject;
    }
    else {
        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
    }

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
        <h1 class="text-4xl font-extrabold dark:text-black pt-5 pb-8">Personal information</h1>

        <%
            if(error) {
        %>

        <p class="text-red-600 text-l font-extrabold py-2">There is one or more error(s) in entering your personal information.</p>

        <%
            }
        %>

        <form action="infoPersonnal-servlet" method="post" class="pt-8">
            <div class="relative z-0 w-full mb-6 group">
                <input type="text" name="nomComplet" id="nomComplet" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " value="<%=user.getName()%>" required />
                <label for="nomComplet" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 rtl:peer-focus:left-auto peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Full name</label>
            </div>
            <div class="relative z-0 w-full mb-6 group">
                <input type="text" name="adresse" id="adresse" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required />
                <label for="adresse" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Adress</label>
            </div>
            <div class="relative z-0 w-full mb-6 group">
                <input type="tel" pattern="[0-9]{3} ?[0-9]{4} ?[0-9]{4}" name="telephone" id="telephone" class="block py-2.5 px-0 w-full text-sm bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer" placeholder=" " required />
                <label for="telephone" class="peer-focus:font-medium absolute text-sm text-gray-500 dark:text-gray-400 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:start-0 rtl:peer-focus:translate-x-1/4 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Phone number (xxx xxxx xxxx)</label>
            </div>
            <button type="submit" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Validate</button>
        </form>
    </div>
    <div class="flex-1 w-80"></div>
</div>

</body>
</html>
