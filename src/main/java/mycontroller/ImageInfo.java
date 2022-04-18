package mycontroller;

import java.time.LocalDateTime;

import java.util.List;

public class ImageInfo {
    int id;
    String login;
    String imageUrl;
    String imageText;
    LocalDateTime publishTime;
    Long imageSize;
    String publishStatus;

    int faceCount;
    List<image_face> faces;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Long getImageSize() {
        return imageSize;
    }

    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public int getFaceCount() {
        return faceCount;
    }

    public void setFaceCount(int faceCount) {
        this.faceCount = faceCount;
    }

    public List<image_face> getFaces() {
        return faces;
    }

    public void setFaces(List<image_face> faces) {
        this.faces = faces;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", image_url='" + imageUrl + '\'' +
                ", iamge_text='" + imageText + '\'' +
                ", publish_time=" + publishTime +
                ", image_size=" + imageSize +
                ", publish_status='" + publishStatus + '\'' +
                '}';
    }
}
