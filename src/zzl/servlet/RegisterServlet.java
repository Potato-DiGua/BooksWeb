package zzl.servlet;

import zzl.sql.MySql;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet",urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String name=request.getParameter("name");
        String email=request.getParameter("email");
        String pwd=request.getParameter("pwd");
        System.out.println(pwd);
        MySql.insertData(MySql.REGISTER_SQL,new String[]{email,pwd,name});

        System.out.println(email);
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter pw=response.getWriter();
        pw.println("<h1>success</h1>");
        pw.close();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request,response);
    }
}
