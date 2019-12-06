<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书享-登录</title>
    <%@include file="master/head.jsp"%>
    <script type="text/javascript" src="js/md5.js"></script>
    <link rel="stylesheet" href="/css/sign.css">
</head>
<body>

<% String s = request.getParameter("error");
    if (s != null && s.equals("true")) { %>
<div class="error_div">
    <label class="error_tip">邮箱或密码错误</label>
</div>

<% } %>

<div id="border_div" class="border">
    <div id="main_div">
        <a class="title" href="/">书享</a>
        <div class="input_div">
            <a class="link_select" href="${pageContext.request.contextPath}/sign_in.jsp">登录</a>
            <b>·</b>
            <a class="link" href="${pageContext.request.contextPath}/sign_up.html">注册</a>
        </div>


        <form autocomplete="off" action="/login" method="post" id="form" onsubmit="return check(this)">
            <dl>
                <dt><label>邮箱</label></dt>
                <dd><input class="input_text" type="email" name="email"></dd>
                <dt><label>密码</label></dt>
                <dd><input class="input_text" type="password" id="pwd"></dd>
                <input type="hidden" name="pwd" id="pwd_md5">
            </dl>
            <input class="btn btn_margin" type="submit" value="登录">
        </form>
    </div>
</div>
</body>
</html>
