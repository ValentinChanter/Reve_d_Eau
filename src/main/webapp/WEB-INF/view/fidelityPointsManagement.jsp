<%@ page import="java.util.List" %>
<%@ page import="com.keio.marketplace.entity.Users" %>
<%@ page import="com.keio.marketplace.dao.UsersDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Loyalty points management</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<%
    UsersDAO usersDAO = new UsersDAO();
    List<Users> users = usersDAO.getAllUsers();
%>
<body>

<%@ include file="components/header.jsp" %>

<h1 class="text-5xl font-extrabold dark:text-black px-8 pt-5">Loyalty points management page</h1>

<div class="w-4/5 bg-white shadow flex justify-between items-center py-5">
    <div class="h-full w-full flex items-center">
        <form action="productManagement" method="get">
            <button type="submit" class="ml-2 px-10 bg-blue-500 hover:bg-blue-600 text-white font-bold rounded">
                Product management
            </button>
        </form>
        <form action="fidelityPointsManagement" method="get">
            <button type="submit" class="ml-2 px-10 bg-gray-300 hover:bg-gray-400 text-white font-bold rounded" disabled>
                Loyalty points management
            </button>
        </form>
    </div>
</div>

<p class="dark:text-black px-8 pt-5">You can modify users loyalty points then click on the "Validate" button to apply your changes.</p>

<br>

<div class="flex flex-col">
    <div class="overflow-x-auto">
        <div class="inline-block min-w-full py-2 sm:px-30 lg:px-32">
            <div class="overflow-hidden">
                <table class="min-w-full text-left text-sm font-light">

                    <thead class="border-b font-medium dark:border-neutral-500">
                        <tr>
                            <th scope="col" class="px-6 py-4">Name</th>
                            <th scope="col" class="px-6 py-4">ID</th>
                            <th scope="col" class="px-6 py-4">Email</th>
                            <th scope="col" class="px-6 py-4">Loyalty points</th>
                        </tr>
                    </thead>

                    <form action="updateFidelityPointsManagement-servlet" method="post">

                        <button type="submit" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center">
                            Validate
                        </button>

                        <%
                            for(Users user : users) {
                        %>

                        <tr class="border-b dark:border-neutral-500">
                            <td class="whitespace-nowrap px-6 py-4"> <%= user.getName() %> </td>
                            <td class="whitespace-nowrap px-6 py-4"> <%= user.getId() %> </td>
                            <td class="whitespace-nowrap px-6 py-4"> <%= user.getEmail() %> </td>
                            <td class="whitespace-nowrap px-6 py-4">
                                <input
                                        type="number"
                                        class="block w-32 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-none"
                                        name="<%= user.getId() %>"
                                        value="<%= user.getLoyaltyPoints() %>"
                                        step="1"
                                        min="0"
                                >
                            </td>
                        </tr>

                        <%
                            }
                        %>

                    </form>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
