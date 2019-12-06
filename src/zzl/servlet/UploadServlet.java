package zzl.servlet;

import zzl.beans.BookInfo;
import zzl.beans.User;
import zzl.sql.MySql;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UploadServlet",urlPatterns = {"/upload/book"})
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        User user=(User)request.getSession().getAttribute("user");
        if(user==null)
        {
            PrintWriter pw=response.getWriter();
            pw.println("error");
            pw.flush();
            pw.close();
            return;
        }
        int bookID=Integer.parseInt(request.getParameter("id"));
        BookInfo bookInfo=new BookInfo();
        bookInfo.setName(request.getParameter("book_name"));
        bookInfo.setAuthor(request.getParameter("author"));
        bookInfo.setPublishingHouse(request.getParameter("pub"));
        bookInfo.setDescription(request.getParameter("desc"));
        bookInfo.setCovers(request.getParameter("cover"));
        bookInfo.setPhone(request.getParameter("phone"));
        if(bookInfo.hasNull())
        {
            PrintWriter pw=response.getWriter();
            pw.println("error");
            pw.flush();
            pw.close();
            return;
        }
        int id=MySql.updateBook(bookInfo,user.getId(),bookID);
        response.sendRedirect("/detail.jsp?id="+id);
    }
}
