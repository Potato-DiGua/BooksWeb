<%@ page import="zzl.beans.BookInfo" %>
<%@ page import="zzl.beans.User" %>
<%@ page import="zzl.dao.MySql" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %><%--
  Created by IntelliJ IDEA.
  User: 86543
  Date: 2019/11/27
  Time: 17:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>书享</title>
    <%@include file="master/head.jsp"%>
</head>
<body>
<%@include file="master/header.jsp" %>
<div id="main">
    <%!void error(HttpServletResponse response) {
            try {
                PrintWriter pw = response.getWriter();
                pw.println("Not Find");
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    %>
    <%String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            error(response);
            return;
        }
    %>

    <%@include file="master/left.jsp" %>
    <div id="right" class="border">
        <%BookInfo book = MySql.getBookInfo(id);
            if (book == null) {
                error(response);
            }
            User user = (User) session.getAttribute("user");
            if (user != null && user.getId() == book.getUser().getId()) {
        %>
        <a class="nav_a btn_revise" href="updatebook.jsp?id=<%=id%>">修改</a>
        <%}%>
        <h1 style="font-family:'Microsoft YaHei UI Light';"><%=book.getName()%>
        </h1>

        <div class="divider"></div>
        <div class="font" style="text-align: left;line-height: 15px;width: 100%;height: 200px">
            <div class="cover_div" style="float: left;margin-right: 15px">
                <img class="cover" src="<%=book.getCovers()%>">
            </div>
            <p><span class="pl">作者:</span><%=book.getAuthor()%>
            </p>
            <p><span class="pl">出版社:</span><%=book.getPublishingHouse()%>
            </p>
            <p><span class="pl">分享者:</span><%=book.getUser().getName()%>
            </p>
        </div>
        <h3 style="color: #EA6F5A">内容简介：</h3>
        <div class="text_div">
            <% String[] strings = book.getDescription().split("\n");
                for (String str : strings) {%>
            <p><%=str%></p>
            <%}%>
        </div>
        <h3 style="color: #EA6F5A">联系方式:</h3>
        <div style="line-height: 15px;">
            <p><span class="pl">邮箱:</span><%=book.getUser().getEmail()%></p>
            <p><span class="pl">手机:</span><%=book.getPhone()%></p>
        </div>
    </div>

</div>
<%@include file="master/footer.jsp" %>
</body>
</html>
