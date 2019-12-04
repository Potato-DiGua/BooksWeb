package zzl.servlet;


import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import zzl.beans.Result;
import zzl.beans.User;
import zzl.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "UploadFileServlet", urlPatterns = {"/upload/cover"})
public class UploadFileServlet extends HttpServlet {
    private final static String IMG_DIR_NAME = "images";
    private final static int IMG_SIZE=2<<21;//2M

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            error(response);
            return;
        }
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            writer.close();
            return;
        }
        String path = getServletContext().getRealPath("/");
        String tempPath = path + "temp";
        String imgDirPath = path + IMG_DIR_NAME;
        Result<String> result=new Result<>();
        if (!FileUtils.makeDir(tempPath) || !FileUtils.makeDir(imgDirPath))
            error(response);
        // 创建工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置缓冲区大小，这里是400kb
        factory.setSizeThreshold(4096 * 100);
        // 设置缓冲区目录
        factory.setRepository(new File(tempPath));
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置上传文件的大小 2M
        upload.setFileSizeMax(IMG_SIZE);
        // 中文处理
        upload.setHeaderEncoding("UTF-8");
        // 创建解析器
        // 得到所有的文件
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if (fileName != null) {
                            String suffixName = FileUtils.getExtension(fileName);
                            String imageName = UUID.randomUUID().toString().replace("-", "").toLowerCase() + suffixName;
                            //String imgPath = "e:" + imageName;// 本机
                            String imgPath = imgDirPath + "/" + imageName;
                            //图片地址拼接
                            String imgUrl = IMG_DIR_NAME + "/" + imageName;
                            File imgFile=new File(imgPath);
                            item.write(imgFile);
                            result.setStatus("ok");
                            result.setData(imgUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("error");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter pw=response.getWriter();
        pw.write(new Gson().toJson(result));
        pw.close();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    private void error(HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<p>error</p>");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
