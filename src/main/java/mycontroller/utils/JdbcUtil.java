package mycontroller.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Jdbc操作Mysql工具类
 * 单连接模式
 * 封装常用方法
 */
public class JdbcUtil {
    /**
     * jdbc连接mysql的连接字符串
     */
    String JDBC_URL = "jdbc:mysql://localhost:3306/WebClass?useUnicode=true&characterEncoding=utf8";
    /**
     * mysql数据库用户名
     */
    String JDBC_USER = "root";
    /**
     * mysql数据库密码
     */
    String JDBC_PASSWORD = "gyj990927";

    /**
     * 获取Mysql数据库连接
     * @return 连接对象
     */
    private Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn=DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取单条数据
     * @param sql 执行的sql
     * @return 将单行数据组织成Map返回
     */
    public HashMap<String,Object> getOne(String sql,Object...params){
        //1、获得连接
        Connection conn=this.getConnection();
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        HashMap<String, Object> result = null;
        if(conn!=null){
            try {
                //2、准备执行语句
                statement = conn.prepareStatement(sql);
                //准备参数
                for(int i=0;i<params.length;i++){
                    statement.setObject(i+1,params[i]);
                }
                //3、执行sql得到ResultSet
                resultSet = statement.executeQuery();
                //4、获取sql结果集的元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                //5、初始化结果对象
                result = new HashMap<>();
                //6、按行解析ResultSet到HashMap
                while (resultSet.next()) {
                    //按列解析每行结果
                    Integer columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        //获取每列列名
                        String columnName = metaData.getColumnName(i);
                        result.put(columnName, resultSet.getObject(i));
                    }
                    //单行数据返回，只解析第一行数据
                    break;
                }
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }finally {
                //7、执行完毕，关闭连接
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取列表数据
     * @param sql 执行的sql
     * @return 将列表数据组织成List返回
     */
    public List<HashMap<String,Object>> getList(String sql,Object...params){
        //1、获得连接
        Connection conn=this.getConnection();
        List<HashMap<String, Object>> result=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        if(conn!=null){
            try {
                //2、准备执行语句
                statement = conn.prepareStatement(sql);
                //准备参数
                for(int i=0;i<params.length;i++){
                    statement.setObject(i+1,params[i]);
                }
                //3、执行sql得到ResultSet
                resultSet = statement.executeQuery();
                //4、获取sql结果集的元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                //5、初始化结果对象
                result = new ArrayList<HashMap<String,Object>>();
                //6、按行解析ResultSet到HashMap
                while (resultSet.next()) {
                    //每行创建新数据
                    HashMap<String,Object> rowMap=new HashMap<>();
                    //按列解析每行结果
                    Integer columnCount = metaData.getColumnCount();
                    for (int i = 1; i <=columnCount; i++) {
                        //获取每列列名
                        String columnName = metaData.getColumnName(i);
                        rowMap.put(columnName, resultSet.getObject(i));
                    }
                    //向结果集中写入一行数据
                    result.add(rowMap);
                }
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }finally {
                //7、执行完毕，关闭连接
                try {
                    resultSet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 执行SQL指令：insert/update/delete
     * @param sql 执行的sql
     * @param params sql所需参数
     * @return 返回受影响行数
     */
    public Integer exec(String sql,Object... params){
        //1、获得连接
        Connection conn=this.getConnection();
        PreparedStatement statement=null;
        Integer result=null;
        if(conn!=null){
            try {
                //2、准备执行语句
                statement = conn.prepareStatement(sql);
                //3、准备参数
                for(int i=0;i<params.length;i++){
                    statement.setObject(i+1,params[i]);
                }
                //4、执行sql得到受影响行数
                result=statement.executeUpdate();
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }finally {
                //5、执行完毕，关闭语句和连接
                try {
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 执行SQL指令：insert，并返回自动生成的id值
     * @param sql 执行的sql
     * @param params sql所需参数
     * @return 返回受影响行数
     */
    public Long execInsertAutoId(String sql, Object... params){
        //1、获得连接
        Connection conn=this.getConnection();
        PreparedStatement statement=null;
        ResultSet keySet=null;
        Long result=null;
        if(conn!=null){
            try {
                //2、准备执行语句
                statement= conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                //3、准备参数
                for(int i=0;i<params.length;i++){
                    statement.setObject(i+1,params[i]);
                }
                //4、执行sql得到受影响行数
                statement.executeUpdate();
                //5、解析自动生成的id值
                keySet=statement.getGeneratedKeys();
                while(keySet.next()){
                    result=keySet.getLong(1);
                    break;
                }
            }catch(SQLException sqlException){
                sqlException.printStackTrace();
            }finally {
                //5、执行完毕，关闭连接
                try {
                    keySet.close();
                    statement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
