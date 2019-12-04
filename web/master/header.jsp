<%@ page import="zzl.beans.User" %><%--
  Created by IntelliJ IDEA.
  User: 86543
  Date: 2019/11/27
  Time: 17:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="header">
    <div class="container">
        <a href="/">
            <img src="picture/logo.png" style="float: left;margin-right: 5px;margin-top: 7px" height="50" width="50">
            <h1  class="title_h1">书享</h1>
        </a>
        <div class="search_div">
            <form action="">
                <input type="text" id="search_input" class="search" placeholder="搜索">
                <input type="button" class="search_btn" onclick="highLight()">
            </form>
        </div>
        <div id="menu">
            <% if (session.getAttribute("user") == null) { %>

            <ul class="nav_menu">
                <li><a class="nav_a" href="${pageContext.request.contextPath}/sign_in.jsp">登录</a></li>
                <li><a class="nav_a" href="${pageContext.request.contextPath}/sign_up.html">注册</a></li>
            </ul>
            <% } else { %>
            <p style="line-height: 40px;margin: 0">
                欢迎您,<%
                User user = (User) session.getAttribute("user");
                out.print(user.getName());%>
                <a href="/logout">退出</a>
            </p>

            <% } %>
        </div>
    </div>
</div>