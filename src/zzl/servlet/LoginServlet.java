package zzl.servlet;

import zzl.dao.MySql;
import zzl.beans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet",urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email=request.getParameter("email");
        String pwd=request.getParameter("pwd");
        User user=MySql.login(email,pwd);
        if(user!=null)
        {
            request.getSession().setAttribute("user",user);
            response.sendRedirect("/");
        }
        else
        {
            request.getRequestDispatcher("/sign_in.jsp?error=true").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request,response);
    }
}
