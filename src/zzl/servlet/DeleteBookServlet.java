package zzl.servlet;

import com.google.gson.Gson;
import zzl.beans.Result;
import zzl.beans.User;
import zzl.sql.MySql;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteBookServlet",urlPatterns = {"/delete"})
public class DeleteBookServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Result<Boolean> result=new Result<>();
        String strID=request.getParameter("id");
        result.setStatus("fail");
        if(strID!=null&&!strID.isEmpty())
        {
            User user=(User)request.getSession().getAttribute("user");
            if(user!=null)
            {
                int id=Integer.parseInt(strID);
                if(id>0){
                    result.setStatus("ok");
                    result.setData(MySql.deleteBook(id,user.getId(),user.getPermission().equals("1")));
                }
            }
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        Gson gson=new Gson();
        String json=gson.toJson(result);
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.close();

    }
}
