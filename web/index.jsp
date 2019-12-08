<%@ page import="zzl.beans.User" %>
<%@ page import="zzl.beans.BookInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="zzl.dao.MySql" %><%--
  Created by IntelliJ IDEA.
  User: 86543
  Date: 2019/11/23
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书享</title>
    <%@include file="master/head.jsp"%>
</head>
<body>
<%@ include file="master/header.jsp" %>

<div id="main">

    <%@ include file="master/left.jsp" %>

    <div id="right" class="border">
        <ul class="book_list">
            <% List<BookInfo> list=MySql.getBooks();
                if(list!=null){
                for (BookInfo book:list) {%>
            <li class="book_item">
                <a class="nav_a" href="/detail.jsp?id=<%=String.valueOf(book.getId())%>">
                    <div class="book_item_div font">
                        <div class="cover_div" style="margin: 15px auto">
                            <img class="cover" src="<%=book.getCovers()%>">
                        </div>
                        <p class="book_item_p"><span class="pl">书名:</span><%=book.getName()%></p>
                        <p class="book_item_p"><span class="pl">作者:</span><%=book.getAuthor()%></p>
                    </div>
                </a>
            </li>
            <%}}%>
        </ul>
    </div>
</div>

<%@include file="master/footer.jsp" %>
</body>
</html>
