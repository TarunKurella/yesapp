import javax.servlet.http.*;
import java.io.IOException;

import com.google.gson.*;
import com.google.cloud.sql.core.CoreSocketFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.ws.rs.*;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CORSFilter
 */




public class serverr extends HttpServlet implements Filter {

   static final String url = "jdbc:mysql://remotemysql.com:3306/4fhmgpKSyz";
   //static final String url = "jdbc:mysql://localhost:3306/javabase";
   // static final String username = "root";
    static final String username = "4fhmgpKSyz";

   // static final String password = "babu4321";
    static final String password = "fcu8rPl6hf";
    public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public void  CORSFilter() {

        // TODO Auto-generated constructor stub
    }
    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }
    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("CORSFilter HTTP Request: " + request.getMethod());
        // Authorize (allow) all domains to consume the content
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST");
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
    }
    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
    class Task {


        int p; // priority
        String name;
        String id="";
        Task(int p, String name,String id){

            this.p=p;
            this.name=name;
            this.id=id;
        }
        Task(){
            this.name="";
            this.p=0;
            this.id="dffd";
        }
    }

    class TaskStatus extends Task{
        int status; //0 post // 1 delete
    }

    class Success{
        String text;
        Success(){
            text="success";
        }
    }



    private Gson gson = new Gson();



    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse response)
            throws IOException {
        ArrayList<Task> sendJson = new ArrayList<>();

//        Task task1 = new Task(1,"check flutter",""+getSaltString());
//        Task task2 = new Task(3,"bye flutter",""+getSaltString());
//        Task task3 = new Task(4,"wow flutter",""+getSaltString());
//        Task task4 = new Task(2,"super flutter",""+getSaltString());
//        sendJson.add(task1);
//        sendJson.add(task2);
//        sendJson.add(task3);
//        sendJson.add(task4);
//        String taskJsonString = this.gson.toJson(sendJson);
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        out.print(taskJsonString);
//        out.flush();

        Connection conn = null;
        Statement stmt = null;
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");


            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url,username,password);


            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT p,name,id FROM prior";
            ResultSet rs = stmt.executeQuery(sql);


            while(rs.next()){

                int p  = rs.getInt("p");
                String name = rs.getString("name");
                String id = rs.getString("id");
                sendJson.add(new Task(p,name,id));


            }

            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            String taskJsonString = this.gson.toJson(sendJson);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(taskJsonString);
            out.flush();
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try








    }




    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        System.out.println("Hello This is PUT Response.");
        System.out.println("Hello This is POST Response.");
        BufferedReader hello = request.getReader();
        Gson gson = new Gson();
        TaskStatus gotit = gson.fromJson(hello, TaskStatus.class);
        Connection conn = null;
        Statement stmt = null;
        System.out.println(gotit.status);
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");


            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url,username,password);


            System.out.println("Creating statement...");
            if(gotit.status==1){
            stmt = conn.prepareStatement("UPDATE prior SET p = ? , name = ? WHERE id = ?;");
            ((PreparedStatement) stmt).setInt(1,gotit.p);
            ((PreparedStatement) stmt).setString(2,gotit.name);
            ((PreparedStatement) stmt).setString(3,gotit.id);
            ((PreparedStatement) stmt).executeUpdate();}
            if(gotit.status==0){
                stmt = conn.prepareStatement("delete from prior where id=?;");
                ((PreparedStatement) stmt).setString(1,gotit.id);
                ((PreparedStatement) stmt).executeUpdate();}


            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            String success = this.gson.toJson(new Success());

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(success);
            out.flush();
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try


    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        System.out.println("Hello This is Delete Response.");


    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("Hello This is POST Response.");
        BufferedReader hello = request.getReader();
        Gson gson = new Gson();
        Task gotit = gson.fromJson(hello, Task.class);
        gotit.id=getSaltString();
        Connection conn = null;
        Statement stmt = null;
        System.out.println(gotit.name);
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");


            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(url,username,password);


            System.out.println("Creating statement...");
            stmt = conn.prepareStatement("insert into prior values(?,?,?)");
            ((PreparedStatement) stmt).setInt(1,gotit.p);
            ((PreparedStatement) stmt).setString(2,gotit.name);
            ((PreparedStatement) stmt).setString(3,gotit.id);
            ((PreparedStatement) stmt).executeUpdate();



            stmt.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            String success = this.gson.toJson(new Success());

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(success);
            out.flush();
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }



    }



}

