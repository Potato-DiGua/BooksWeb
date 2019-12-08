package zzl.servlet;

import com.google.gson.Gson;
import zzl.beans.BookInfo;
import zzl.dao.MySql;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BookListServlet",urlPatterns = {"/books"})
public class BookListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request,response);//取消post
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        List<BookInfo> list=MySql.getBooks();
        String json=new Gson().toJson(list);
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.close();
    }
}
