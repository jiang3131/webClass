package mycontroller.service;

import com.baidu.aip.face.AipFace;
import org.json.JSONObject;

import java.util.HashMap;

public class BaiduAIService {
    public static final String APP_ID = "25869006";
    public static final String API_KEY = "HoMCcNGZsEbYXXuwvBPbqSyk";
    public static final String SECRET_KEY = "DYQQe6hhNsqBmx58UqrZxmf2ZKjZUjOw";
    public JSONObject checkFaces(String imageBase64){
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
        // 传入可选参数调用接口
        HashMap options = new HashMap();
        options.put("face_field", "age,beauty,expression,face_shape,gender,glasses,landmark,landmark72,landmark150,quality,eye_status,emotion,face_type");
        options.put("max_face_num", "10");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "NONE");
        String imageType = "BASE64";
        // 人脸检测
        JSONObject res = client.detect(imageBase64, imageType, options);
        return res;

    }
    public JSONObject checkFaceWithUrl(String url){
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        HashMap<String, Object> options = new HashMap<>();
        options.put("face_field", "age,beauty,expression,gender,glasses,emotion,face_type,face_token");
        options.put("max_face_num", "10");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "NONE");

        String imageUrl = url;
        String imageType = "URL";
        JSONObject res = client.detect(imageUrl, imageType, options);
        return res;
    }

}
