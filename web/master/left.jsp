<%--
  Created by IntelliJ IDEA.
  User: 86543
  Date: 2019/11/27
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="left" class="border">
    <div id="nav_div">
        <ul class="nav_menu">
            <li>
                <a class="nav_a" href="/">首页</a>
            </li>
            <li>
                <a class="nav_a" href="${pageContext.request.contextPath}/updatebook.jsp">分享</a>
                <ul class="second_menu">
                    <li>
                        <a class="nav_a" href="${pageContext.request.contextPath}/updatebook.jsp">分享图书</a>
                    </li>
                </ul>
            </li>
            <%
                if (session.getAttribute("user")!= null) {
            %>
            <li>
                <a class="nav_a">个人中心</a>
                <ul class="second_menu">
                    <li>
                        <a class="nav_a" href="${pageContext.request.contextPath}/manage.jsp">管理</a>

                    </li>
                </ul>
            </li>
            <%
                }
            %>
        </ul>
    </div>
</div>
