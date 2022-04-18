package mycontroller.apis;

import cn.edu.njuit.web.server.tools.ImageTool;
import com.baidu.aip.face.AipFace;
import mycontroller.service.BaiduAIService;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;

import static javax.crypto.Cipher.SECRET_KEY;

@WebServlet(name = "FaceServlet", value = "/api/face")
@MultipartConfig
public class FaceServlet extends HttpServlet {

    public static final String APP_ID = "25869006";
    public static final String API_KEY = "HoMCcNGZsEbYXXuwvBPbqSyk";
    public static final String SECRET_KEY = "DYQQe6hhNsqBmx58UqrZxmf2ZKjZUjOw";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part image= request.getPart("image");
        ImageTool imageTool=new ImageTool();
        String imageBase64 = imageTool.image2Base64(image.getInputStream());
        BaiduAIService baiduAIService = new BaiduAIService();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(baiduAIService.checkFaces(imageBase64).toString());
    }
}

