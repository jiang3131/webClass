package mycontroller.apis;

import mycontroller.ImageInfo;
import mycontroller.service.BaiduAIService;
import mycontroller.service.BosService;
import mycontroller.service.ListServiece;
import mycontroller.utils.Jdbc;
import mycontroller.utils.JdbcUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MultipartConfig
@WebServlet(name = "ImageInfoAPI", value = "/api/image-info")
public class ImageInfoAPI extends HttpServlet {
    JdbcUtil jdbcUtil = new JdbcUtil();
    BosService bosService = new BosService();
    BaiduAIService baiduAIService = new BaiduAIService();
    Jdbc jdbc = new Jdbc();
    ListServiece listServiece = new ListServiece();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String imageId = request.getParameter("imageId");
            if (imageId == null) {
                String sql = "select * from image_info";
                List<HashMap<String, Object>> imagelist = jdbcUtil.getList(sql);
                List<ImageInfo> result = listServiece.getImageinfo(imagelist, imageId);
                JSONArray jsonArray = new JSONArray(result);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(jsonArray.toString());
            } else {
                String sql1 = "select * from image_info where id=?";
                List<HashMap<String, Object>> imagelist = jdbcUtil.getList(sql1, imageId);
                List<ImageInfo> imageInfos = listServiece.getImageinfo(imagelist, imageId);
                ImageInfo imageInfo = imageInfos.get(0);
                JSONObject jsonObject = new JSONObject(imageInfo);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Part image = request.getPart("image");
            String login = request.getParameter("login");
            String imageText = request.getParameter("imageText");
            String publishStatus = request.getParameter("publishStatus");
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setLogin(login);
            imageInfo.setImageText(imageText);
            imageInfo.setPublishStatus(publishStatus);
            String imageName = image.getSubmittedFileName();
            String random = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            String fileName = random + imageName.substring(imageName.lastIndexOf("."));
            String s = bosService.putObject(image.getInputStream(), fileName);
            imageInfo.setImageUrl(s);
            imageInfo.setPublishTime(LocalDateTime.now());
            imageInfo.setImageSize(22222L);
            //把图片存入数据库
            String sql = "insert into image_info(login,image_url,image_text,publish_time,image_size,publish_status) values(" +
                    "?,?,?,?,?,?)";
            Object[] canshu = {imageInfo.getLogin(), imageInfo.getImageUrl(), imageInfo.getImageText(), imageInfo.getPublishTime(), imageInfo.getImageSize(), imageInfo.getPublishStatus()};
            imageInfo.setId((jdbcUtil.execInsertAutoId(sql, canshu)).intValue());
            //人脸检测
            JSONObject faceJson1 = baiduAIService.checkFaceWithUrl(imageInfo.getImageUrl()).getJSONObject("result");
            String[] names = JSONObject.getNames(faceJson1);
            JSONArray faceJson = faceJson1.getJSONArray(names[1]);
            Map<String, Object> faceMap = faceJson.getJSONObject(0).toMap();
            String age = String.valueOf(faceMap.get("age"));
            String beauty = String.valueOf(faceMap.get("beauty")).substring(0, 2);
            String expression = String.valueOf(faceMap.get("expression"));
            String gender = String.valueOf(faceMap.get("gender"));
            String glasses = String.valueOf(faceMap.get("glasses"));
            String emotion = String.valueOf(faceMap.get("emotion"));
            String face_type = String.valueOf(faceMap.get("face_type"));
            String face_token = String.valueOf(faceMap.get("face_token"));
            String[] facecanshu = {age, beauty, expression, gender, glasses, emotion, face_type, face_token};
            for (int i = 0; i < facecanshu.length; i++) {
                facecanshu[i] = facecanshu[i].replaceAll("}", "").substring(String.valueOf(facecanshu[i]).lastIndexOf("=") + 1);
            }//插入数据库
            String sql1 = "insert into image_face(face_token,face_age,face_beauty,face_type,emotion,expression,glasses,gender,face_time,image_id) values" +
                    "( ?,?,?,?,?,?,?,?,?,?)";
            Object[] canshu2 = {facecanshu[7], Integer.parseInt(facecanshu[0]), Integer.parseInt(facecanshu[1]), facecanshu[6], facecanshu[5], facecanshu[2], facecanshu[4], facecanshu[3], LocalDateTime.now(), imageInfo.getId()};
            jdbcUtil.execInsertAutoId(sql1, canshu2);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image_size", imageInfo.getImageSize());
            jsonObject.put("id", imageInfo.getId());
            jsonObject.put("image_url", imageInfo.getImageUrl());
            jsonObject.put("iamge_text", imageInfo.getImageText());
            jsonObject.put("publish_status", imageInfo.getPublishStatus());
            jsonObject.put("publish_time", imageInfo.getPublishTime());
            jsonObject.put("login", imageInfo.getLogin());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String UserLogin = token.split("\\.")[1];
        String sql = "select * from image_info where login = ?";
        String imageLogin = "";
        PreparedStatement ps2 = jdbc.getPs(sql);
        ResultSet rs;
        try {
            ps2.setString(1, UserLogin);
            rs = ps2.executeQuery();
            while (rs.next()) {
                imageLogin = rs.getString("login");
                System.out.println(imageLogin);
                System.out.println(UserLogin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (imageLogin.equals(UserLogin)) {
            String imageId = req.getParameter("imageId");
            //language=MySQL
            String sql2 = "delete from image_face where image_id =? ";
            //language=MySQL
            String sql1 = "delete from image_info where id =? ";
            int result = -1;
            int result_two = -1;

            PreparedStatement ps = jdbc.getPs(sql2);
            PreparedStatement ps1 = jdbc.getPs(sql1);
            try {
                ps.setString(1, imageId);
                ps1.setString(1, imageId);
                result = ps.executeUpdate();
                result_two = ps1.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (result > -1 && result_two > -1) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", "ok");
                resp.setContentType("application/json;charset=utf-8");
                resp.getWriter().write(jsonObject.toString());
            }
        }


    }
}
