<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@page import="ua.olharudenko.libraryapp.models.User"%>
<%@page import="ua.olharudenko.libraryapp.models.Order"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.apache.taglibs.standard.tag.rt.fmt.BundleTag"%>
<%@ page import="java.util.*" import="java.io.*"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setBundle basename="resources"/>

<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
<title>VISITOR</title>

<!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
</head>

<body>
<br>
<% String loc = (String) request.getSession().getAttribute("userLocale"); %>
<% Long userId = (Long) request.getSession().getAttribute("userId"); %>

<input type="hidden" name="locale" value="login"/>
<input type="hidden" id="locale" th:field="*{locale}" th:value="${loc}"/>

<jsp:useBean id="user" class="ua.olharudenko.libraryapp.models.User" scope="session"></jsp:useBean>
<jsp:setProperty property="*" name="user"/>

<div class="album py-5 bg-light">
        <div class="container">

<p class="lead text-muted"><fmt:message key="visitor"/></p>
<p class="lead text-muted"> +<jsp:getProperty property="phone" name="user"/><br></p>
<p class="lead text-muted"> <jsp:getProperty property="email" name="user"/><br></p>

    <section class="py-5 text-center container">
<br>
<h1 class="fw-light">
<jsp:getProperty property="firstName" name="user"/>
<jsp:getProperty property="lastName" name="user"/><br>
</h1>
<br><br>
<a href="templates/user/user_profile_update.jsp">update</a>
<a href="/libraryApp/controller?command=logout">logout</a>
    </section>
        </div>
</div>

<div class="container">

    	<input type="hidden" name="command" value="viewAllBooks"/>
    	<input type="hidden" name="userId" value="${userId}"/>
    	<input type="hidden" name="userRole" value="${userRole}"/>

        <table class="table table-striped">
            <thead>
                <tr><td><h3>My Orders:</h3></td></tr>
                <tr class="tr tr-success">
                    <td>Book</td>
                    <td>Authors</td>
                    <td>Status</td>
                    <td>is confirm</td>
                </tr>
            </thead>

            <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td><a class="btn btn-danger" href="/libraryApp/controller?command=viewBookProfile&id=${order.getBook().id}">
                        ${order.getBook().title}
                        </a></td>
                        <td><a class="btn btn-danger" href="/libraryApp/controller?command=viewAuthorProfile&id=${order.getBook().getLocalizedAuthor().authorId}&locale=${order.getBook().publishLocale}">
                        ${order.getBook().getLocalizedAuthor().fullName}</a>
                        </td>
                        <td>${order.orderStatus}</td>
                        <td>${order.adminOrderStatus}</td>
                        <td>
                        <a class="btn btn-danger" onclick="return confirm('Are you sure you want to delete?')" href="/libraryApp/controller?command=deleteOrder&id=${order.id}">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

<div>
<table><tr>
<td>
<form class="" action="/libraryApp/controller" method="post">
<input type="hidden" name="command" value="viewAllAuthors"/>
<input type="hidden" name="userId" value="${userId}"/>
<input type="hidden" name="userRole" value="${user.getRole().toString()}"/>
<button class="w-100 mb-2 btn btn-lg rounded-4 btn-primary" type="submit">all authors</button>
</form>
</td>

<td>
<form class="" action="/libraryApp/controller" method="post">
<input type="hidden" name="command" value="viewAllBooks"/>
<input type="hidden" name="userRole" value="${user.getRole().toString()}"/>
<input type="hidden" name="userId" value="${user.getId()}"/>
<button class="w-100 mb-2 btn btn-lg rounded-4 btn-primary" type="submit">all books</button>
</form>
</td>

<td>
<form class="" action="/libraryApp/controller" method="post">
<input type="hidden" name="command" value="viewAllOrders"/>
<input type="hidden" name="userRole" value="${user.getRole().toString()}"/>
<input type="hidden" name="userId" value="${user.getId()}"/>
<button class="w-100 mb-2 btn btn-lg rounded-4 btn-primary" type="submit">all my orders</button>
</form>
</td>
</tr></table>
</div

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>