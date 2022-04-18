package mycontroller.jdbcDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import mycontroller.utils.JdbcTwo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcDemo {
   static JdbcTwo myjdbc = new JdbcTwo();
    public static void getMoviesPrint(){
        ResultSet rs=null;
        ResultSetMetaData rs1=null;
        String sql ="SELECT\n"+
                "\ta.movie_id,a.name,a.name_en,img,rating,length,movie_publish_date,movie_year\n"+
                "FROM\n"+
                "\tmovie_info a,movie_types b,type_info c\n"+
                "WHERE\n"+
                "\ta.movie_id = b.movie_id AND b.type_id = c.type_id AND c.NAME = '喜剧' AND a.movie_year >=2000 AND a.rating>8";
        PreparedStatement ps = myjdbc.getPs(sql);

        try {
             rs =ps.executeQuery();
             rs1=rs.getMetaData();
            Integer columnCount = rs1.getColumnCount();
            int row =0;
            while (rs.next()){
                System.out.println("\n读到了一行数据"+((row++)+1));
                for(int i=1;i<=columnCount;i++){
                    String value = rs.getString(i);
                    System.out.print(value+"\t");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            myjdbc.close(rs);
            myjdbc.close();
        }
    }
    public static List<Map<String,Object>> getMovieList(){
        String sql ="SELECT\n"+
                "\ta.movie_id,a.name,a.name_en,img,rating,length,movie_publish_date,movie_year\n"+
                "FROM\n"+
                "\tmovie_info a,movie_types b,type_info c\n"+
                "WHERE\n"+
                "\ta.movie_id = b.movie_id AND b.type_id = c.type_id AND c.NAME = '喜剧' AND a.movie_year >=2000 AND a.rating>8";
        PreparedStatement ps = myjdbc.getPs(sql);
        List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
        try {
            ResultSet resultSet=ps.executeQuery();
            ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
            Integer columnCount = resultSetMetaData.getColumnCount();

            while(resultSet.next()){
                Map<String,Object> rowMap=new HashMap<String,Object>();
                for(int i=1;i<=columnCount;i++){
                    rowMap.put(resultSetMetaData.getColumnName(i),resultSet.getObject(i));
                }
                result.add(rowMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            myjdbc.close();

        }


        return result;
    }
    public static List<MovieInfo> getMovies(){
        List<Map<String, Object>> sqlList = getMovieList();
        List<MovieInfo> result = new ArrayList<MovieInfo>();
        for(int i=0;i<sqlList.size();i++){
            Map<String,Object> rowMap = sqlList.get(i);
            MovieInfo movieInfo=new MovieInfo();
            movieInfo.setMovieId(TypeUtils.castToLong(rowMap.get("movie_id")));
            movieInfo.setMovieName(TypeUtils.castToString(rowMap.get("name")));
            movieInfo.setMovieImage(TypeUtils.castToString(rowMap.get("img")));
            movieInfo.setMovieLength(TypeUtils.castToInt(rowMap.get("length")));
            movieInfo.setMovieRating(TypeUtils.castToFloat(rowMap.get("rating")));
            movieInfo.setMovieYear(TypeUtils.castToString(rowMap.get("movie_year")));
            movieInfo.setMovieEnglisgName(TypeUtils.castToString(rowMap.get("name_en")));
            movieInfo.setMoviePublishiTime(TypeUtils.castToString(rowMap.get("movie_publish_date")));
            result.add(movieInfo);
        }
        return result;
    }


    public static void main(String[] args) {
//getMoviesPrint();
//        List<MovieInfo> movieList = getMovies();
//        String s = JSON.toJSONString(movieList);
//        System.out.println(s);


    }
}
