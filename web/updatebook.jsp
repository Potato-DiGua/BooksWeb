<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>
<%@ page import="zzl.beans.BookInfo" %>
<%@ page import="zzl.dao.MySql" %><%--
  Created by IntelliJ IDEA.
  User: 86543
  Date: 2019/11/30
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书享</title>
    <%@include file="master/head.jsp" %>
    <style type="text/css">
        tr {
            height: 40px;
        }

        #right {
            text-align: center;
        }

        #submit {
            margin-top: 60px;
        }

        .update_btn {
            position: absolute;
            height: 100%;
            width: 100%;
            line-height: 200px;
            background: rgba(0, 0, 0, 0.3);
            text-align: center;
            color: white;
        }

        #div_select_cover {
            width: 135px;
            max-height: 200px;
            height: 200px;
            margin: 15px;
            position: relative;
            border: 1px solid #e1e4e8;

        }

        #div_select_cover div {
            position: absolute;
            z-index: 3;
            top: 0;
            left: 0;
        }

        #div_select_cover:hover .update_btn {
            display: block;
        }

        .fl {
            float: left;
            margin-bottom: 15px;
        }

        #tip_p {
            top: 0;
            line-height: 200px;
            margin: 0;
            width: 135px;
            position: absolute;
            z-index: 2;
            left: 0;
        }

        #update_tip {
            display: none;
            text-align: left;
            color: red;
        }
    </style>
</head>
<body>
<%@include file="master/header.jsp" %>
<div id="main">
        <%!
        void error(HttpServletResponse response) {
            try {
                PrintWriter pw = response.getWriter();
                pw.println("error");
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    %>
        <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/sign_in.jsp");
        }

        BookInfo book=null;
        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) {
            book = MySql.getBookInfo(id);
            if(user.getId()!=book.getUser().getId())
            {
                error(response);
            }
        }else
            {
                id=null;
            }

    %>

    <%@include file="master/left.jsp" %>
    <div id="right" class="border">
        <div class="fl">
            <div id="div_select_cover">
                <div class="cover_div">
                    <img id="img_cover" class="cover" src="<%=book!=null?book.getCovers():""%>">
                </div>
                <p id="tip_p">封面</p>
            </div>
            <form id="update_form" enctype="multipart/form-data" onsubmit="return false;">
                <button onclick="uploadFile()">上传</button>
                <input id="upload_file" name="cover" type="file" accept="image/png,image/jpg,image/jpeg"/>
            </form>
            <p id="update_tip"></p>
        </div>
        <form method="post" action="/upload/book" onsubmit="return checkBook(this)">
            <input name="cover" type="hidden" id="input_cover" value="<%=book!=null?book.getCovers():""%>">
            <table>
                <tr>
                    <td>
                        <span class="pl">书名：</span>
                    </td>
                    <td>
                        <input autocomplete="false" name="book_name" class="input_text" type="text" maxlength="45"
                               value="<%=book!=null?book.getName():""%>">
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="pl">作者：</span>
                    </td>
                    <td>
                        <input autocomplete="false" name="author" class="input_text" type="text" maxlength="20"
                               value="<%=book!=null?book.getAuthor():""%>">
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="pl">出版社：</span>
                    </td>
                    <td>
                        <input autocomplete="false" name="pub" class="input_text" type="text" maxlength="45"
                               value="<%=book!=null?book.getPublishingHouse():""%>">
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="pl">手机：</span>
                    </td>
                    <td>
                        <input autocomplete="false" name="phone" class="input_text" type="text" maxlength="11"
                               value="<%=book!=null?book.getPhone():""%>" onfocusout="setPhone(this)">
                        <span id="error_phone_tip" style="color:red;display: none;">请输入有效手机号码</span>
                    </td>
                </tr>
                <tr>
                    <td style="vertical-align: top"><span class="pl">简介：</span></td>
                    <td>
                        <textarea name="desc" rows="10" cols="80" minlength="10" maxlength="500"
                                  placeholder="请输入至少10个字符，最多500个字符。"
                                  style="font-size: 18px"><%=book != null ? book.getDescription() : ""%></textarea>
                    </td>
                </tr>
            </table>
            <input id="submit" class="btn" type="submit" value="提交">
            <input name="id" type="hidden" value="<%=id!=null?id:"-1"%>">
        </form>
    </div>
    <%@include file="master/footer.jsp" %>
    <script>
        let upload = document.getElementById('upload_file');
        upload.addEventListener('change', function () {
            var file;
            file = this.files[0];
            let p = document.getElementById("update_tip");
            if (file.size < 1024 * 1024 * 2) { // 非常简单的交验，判断文件是否是图片
                p.style.display = "none";
                console.log(file.size);
                var reader = new FileReader();//文件预览对象
                reader.readAsDataURL(file);//设置要预览的文件
                reader.onload = function (e) {//监听文件加载完成事件
                    let img = document.getElementById("img_cover");
                    img.src = e.target.result;
                };
            } else {
                upload.value = null;
                p.innerText = "文件大小必须小于2M";
                p.style.display = "block";
            }
        });
    </script>
</body>
</html>
