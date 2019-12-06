<%@ page import="java.util.List" %>
<%@ page import="zzl.sql.MySql" %>
<%@ page import="zzl.beans.BookInfo" %>
<%@ page import="java.io.PrintWriter" %><%--
  Created by IntelliJ IDEA.
  User: 86543
  Date: 2019/12/2
  Time: 0:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书享</title>
    <%@include file="master/head.jsp" %>
    <style type="text/css">
        .a_text {
            line-height: 50px;
            color: black;
            text-align: left;
            width: 500px;
            display: inline;
        }

        .a_text:hover {
            color: #EF6F61;
        }

        .fr {
            height: 30px;
            margin-top: 20px;
            float: right;
            display: inline-block;
            vertical-align: top;
        }

        .fr a {
            margin: 0 5px;
            line-height: 30px;
            color: #349EDF;
            display: inline-block;
        }

        li div {
            height: 50px;
            border-bottom: 1px solid #e1e4e8;
        }
    </style>
</head>
<body>
<%@include file="master/header.jsp" %>
<div id="main">
    <%@include file="master/left.jsp" %>
    <div id="right" class="border">
        <ul class="nav_menu">
            <%
                User user = (User) session.getAttribute("user");
                boolean isAdmin = false;
                if (user == null) {
                    response.sendRedirect("/sign_in.jsp");
                    return;
                }
                String type = request.getParameter("type");
                List<BookInfo> list = null;
                if (type != null && type.equals("admin")) {
                    //判断是否为管理权限
                    if (user.getPermission().equals("1")) {
                        isAdmin = true;
                        list = MySql.getAllBooks();
                    } else {
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter pw = response.getWriter();
                        pw.println("You aren't administrator");
                        pw.close();
                        return;
                    }

                } else {
                    list = MySql.getUserShareBooks(user.getId());
                }
                for (BookInfo book : list) {
            %>
            <li>
                <div>
                    <a class="nav_a a_text" href="/detail.jsp?id=<%=book.getId()%>"><%=book.getName()%>
                        -<%=book.getAuthor()%>
                        -<%=book.getPublishingHouse()%>
                    </a>
                    <div class="fr">
                        <a class="nav_a" href="/detail.jsp?id=<%=book.getId()%>">查看</a>
                        <%if (!isAdmin) {%>
                        <a class="nav_a" href="/updatebook.jsp?id=<%=book.getId()%>">编辑</a>
                        <%}%>
                        <a class="nav_a" style="color: red" href="javascript:void(0)"
                           onclick="deleteBook(this,<%=book.getId()%>)">删除</a>
                    </div>
                </div>

            </li>

            <%
                }
            %>
        </ul>
    </div>
</div>
<%@include file="master/footer.jsp" %>
</body>
</html>
