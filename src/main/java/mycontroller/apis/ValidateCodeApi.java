package mycontroller.apis;

import mycontroller.ValidateCode;
import mycontroller.service.ValidateCodeService;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ValidateCodeApi", value = "/api/validate-code")
public class ValidateCodeApi extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //产生验证码
        ValidateCodeService validateCodeService=new ValidateCodeService();
        ValidateCode validateCode = validateCodeService.createValidate();
        //将验证码返回
        response.setContentType("application/json;charset=utf-8");
        JSONObject result = new JSONObject(validateCode);
        response.getWriter().write(result.toString());
    }

}
