package mycontroller;

public class ValidateCode {
    String randomCode;
    String imageUrl;

    public ValidateCode() {
    }

    public  ValidateCode(String randomCode){
        this.randomCode = randomCode;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRamdamCode(String randamCode) {
        this.randomCode = randamCode;
    }

    public String getImageUrl() {
        this.imageUrl = "/image/validate-code?code="+this.randomCode;
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
