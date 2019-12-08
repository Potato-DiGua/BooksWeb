package zzl.servlet;

import com.google.gson.Gson;
import zzl.beans.Result;
import zzl.dao.MySql;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CheckEmailServlet",urlPatterns = {"/checkemail"})
public class CheckEmailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email=request.getParameter("email");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        boolean check= MySql.checkEmail(email);
        Result<Boolean> result=new Result<>("ok",check);
        Gson gson=new Gson();
        String json=gson.toJson(result);
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.close();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request,response);
    }
}
