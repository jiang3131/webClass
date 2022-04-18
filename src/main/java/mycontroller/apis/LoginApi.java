package mycontroller.apis;


import com.alibaba.fastjson.JSONObject;
import mycontroller.PasswordEncoder;
import mycontroller.service.UserTokenService;
import mycontroller.service.ValidateCodeService;
import mycontroller.utils.Jdbc;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LoginApi", value = "/api/login")
@MultipartConfig
public class LoginApi extends HttpServlet {
    Jdbc myjdbc=new Jdbc();
    ResultSet rs;
    ValidateCodeService validateCodeService=new ValidateCodeService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader br=new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
        StringBuffer jsonString = new StringBuffer();
        String line = null;
        while((line=br.readLine())!=null){
            jsonString.append(line);
        }
        JSONObject loginJson = JSONObject.parseObject(jsonString.toString());
        String login = loginJson.getString("login");
        String password = PasswordEncoder.encode(loginJson.getString("password"));
        String randomCode = loginJson.getString("randomCode");
        String validateCode = loginJson.getString("validateCode");
        if(validateCodeService.validate(randomCode,validateCode)){
            String sql = "select * from user_account where login = ? AND password = ?";
            PreparedStatement ps = myjdbc.getPs(sql);
            try {
                ps.setString(1, login);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if (rs.next()) {
                    UserTokenService userTokenService = new UserTokenService();
                    String token = userTokenService.getToken(login);
                    JSONObject result = new JSONObject();
                    result.put("token", token);
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().print(result.toString());
                } else {
                    org.json.JSONObject error = new org.json.JSONObject();
                    error.put("error", "用户名密码不存在");
                    response.setStatus(401);
                    response.setContentType("applicaton/json;charset=utf-8");
                    response.getWriter().print(error.toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            org.json.JSONObject error = new org.json.JSONObject();
            error.put("error", "验证码错误");
            response.setStatus(400);
            response.setContentType("applicaton/json;charset=utf-8");
            response.getWriter().print(error.toString());

        }
    }
}
