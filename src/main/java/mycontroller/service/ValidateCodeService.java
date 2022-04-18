package mycontroller.service;


import mycontroller.ValidateCode;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class ValidateCodeService {//产生验证码
    public static HashMap<String,String>  ValidateCodeStore = new HashMap<>();
    public ValidateCode createValidate(){
        Integer number1=0;
        Integer number2=0;
        String imageText="";
        String result="";
        String randomCode = UUID.randomUUID().toString();
        Random random = new Random();
        int num=random.nextInt(4)+1;
        switch (num){
            case 1:
                number1 = random.nextInt(100);
                number2 = random.nextInt(100);
                imageText = number1+"+"+number2+"=?";
                result = String.valueOf(number1+number2);
                break;
            case 2:
                number1 = random.nextInt(20);
                number2 = random.nextInt(20);
                imageText = number1+"*"+number2+"=?";
                result = String.valueOf(number1*number2);
                break;
            case 3:
                number1 = random.nextInt(20);
                number2 = random.nextInt(20);

                if(number1>number2){
                    imageText = number1+"-"+number2+"=?";
                    result = String.valueOf(number1-number2);
                }else {
                    imageText = number2+"-"+number1+"=?";
                    result = String.valueOf(number2-number1);
                }
                break;
        }
        ValidateCodeStore.put(randomCode,imageText+","+result);
        ValidateCode validateCode = new ValidateCode(randomCode);
        return validateCode;
    }
    public boolean validate(String randomCode,String validateCode){//判断验证码
        if(ValidateCodeStore.containsKey(randomCode)){
            String validateString = ValidateCodeStore.get(randomCode);
            String answer = validateString.split(",")[1];
            ValidateCodeStore.remove(randomCode);
            return  answer.equals(validateCode);
        }
        return false;
    }
    public String getValidateText(String randomCode){//根据随机码获取验证码算式文本
        if(ValidateCodeStore.containsKey(randomCode)){
            String validateString = ValidateCodeStore.get(randomCode);
            return validateString.split(",")[0];
        }
        return null;
    }

}
