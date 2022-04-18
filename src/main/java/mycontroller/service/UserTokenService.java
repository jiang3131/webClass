package mycontroller.service;

import mycontroller.User;
import mycontroller.utils.Jdbc;

public class UserTokenService {
    Jdbc myjdbc=new Jdbc();
    //拿到令牌
    public String getToken(String login){
        return  "thisisatoken."+login+".tokenend";
    }
    public User getUser(String token){
        try {
            String login = token.split("\\.")[1];
            return new User(login);
        }catch (Exception e){
            e.printStackTrace();
        }
          return null;
    }
}
