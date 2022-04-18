package mycontroller.apis;

import cn.edu.njuit.web.server.tools.ImageTool;
import mycontroller.service.ValidateCodeService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@WebServlet(name = "ValidateImageApi", value = "/image/validate-code")
public class ValidateImageApi extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //解析dode参数
        String randomCode = request.getParameter("code");
        //调用验证码服务器获得算式文本
        ValidateCodeService validateCodeService = new ValidateCodeService();
        String imagetext = validateCodeService.getValidateText(randomCode);
        if(imagetext!=null){
            //调用字符串生成的方法
            ImageTool imageTool = new ImageTool();
            Integer width = imagetext.length()*16;
            Integer height = 40;
            ByteArrayOutputStream image = imageTool.string2Image(imagetext, width, height);
            response.setContentType("image/jpeg");
            image.writeTo(response.getOutputStream());


        }
    }
}


