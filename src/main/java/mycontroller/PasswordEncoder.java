package mycontroller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    private final static char[] HEX="0123456789abcdef".toCharArray();
   public static String encode(String password){
       try {
           MessageDigest md5= MessageDigest.getInstance("MD5");
           md5.update(password.getBytes());
           return byte2Hex(md5.digest());
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return null;
   }
private static String byte2Hex(byte [] bytes){
       char[] chs = new char[bytes.length*2];
    for (int i = 0,offset = 0; i <bytes.length ; i++) {
        chs[offset++]=HEX[bytes[i]>>4 & 0xf];
        chs[offset++]=HEX[bytes[i]&0xf];
    }
    return new String(chs);
}

    public static void main(String[] args) {
       int i=0;
       int a=0;
       Long  b= Long.valueOf(a);
        while(i<=10000000){
            b=b+(i++);
        }
        System.out.println(b);
    }

}
