package mycontroller.service;

import com.alibaba.fastjson.util.TypeUtils;
import mycontroller.ImageInfo;
import mycontroller.image_face;
import mycontroller.utils.JdbcUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListServiece {
    JdbcUtil jdbcUtil = new JdbcUtil();

    public List<ImageInfo> getImageinfo(List<HashMap<String, Object>> list, String j) {
        if (j==null){
            j="-1";
        }
        String sql = "select * from image_face where image_id=? ";
        List<HashMap<String, Object>> facelist = jdbcUtil.getList(sql, Integer.parseInt(j));
        int faceNum = facelist.size();
        List<image_face> faceresult = getImage_face(facelist);
        List<ImageInfo> result = new ArrayList<ImageInfo>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> rowMap = list.get(i);
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setId(TypeUtils.castToInt(rowMap.get("id")));
            imageInfo.setLogin(TypeUtils.castToString(rowMap.get("login")));
            imageInfo.setImageUrl(String.valueOf(rowMap.get("image_url")));
            imageInfo.setImageText(String.valueOf(rowMap.get("image_text")));
            imageInfo.setPublishTime((LocalDateTime) rowMap.get("publish_time"));
            imageInfo.setImageSize(22L);
            imageInfo.setPublishStatus(TypeUtils.castToString(rowMap.get("publish_status")));
            imageInfo.setFaceCount(faceNum);
            imageInfo.setFaces(faceresult);
            result.add(imageInfo);
        }
        return result;
    }

    public List<image_face> getImage_face(List<HashMap<String, Object>> list) {
        List<image_face> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> rowMap = list.get(i);
            image_face Image_face = new image_face();
            Image_face.setId(TypeUtils.castToInt(rowMap.get("id")));
            Image_face.setFaceToken(TypeUtils.castToString(rowMap.get("face_token")));
            Image_face.setFaceAge(TypeUtils.castToInt(rowMap.get("face_age")));
            Image_face.setFaceBeauty(TypeUtils.castToInt(rowMap.get("face_beauty")));
            Image_face.setFaceType(TypeUtils.castToString(rowMap.get("face_type")));
            Image_face.setEmotion(TypeUtils.castToString(rowMap.get("emotion")));
            Image_face.setExpression(TypeUtils.castToString(rowMap.get("expression")));
            Image_face.setGlasses(TypeUtils.castToString(rowMap.get("glasses")));
            Image_face.setGender(TypeUtils.castToString(rowMap.get("gender")));
            Image_face.setFaceTime(TypeUtils.castToString(rowMap.get("face_time")));
            Image_face.setImageId(TypeUtils.castToInt(rowMap.get("image_id")));
            result.add(Image_face);
        }
        return result;
    }
}
