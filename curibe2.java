//STEP 1. Import required packages
import java.sql.*;

public class curibe2 {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/test";

   //  Database credentials
   static final String USER = "carlos";
   static final String PASS = "cmuribjx";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      System.out.println("Connected database successfully...");
      
      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();

      String sql30 = "CREATE TABLE customer (ssn int(12), cname char(50), gender char(50), age int(4), profession char(50))";
      String sql31 = "CREATE TABLE download (ssn int(12), sid int(20), time int(5))";
      String sql32 = "CREATE TABLE song (sid int(20), sname char(50), singer char(20), price int(10))";
      stmt.executeUpdate(sql30);
      stmt.executeUpdate(sql31);
      stmt.executeUpdate(sql32);

      String sql33 = "LOAD DATA LOCAL INFILE '/Users/Carlos/Documents/customer.txt' into table customer"; 
      String sql34 = "LOAD DATA LOCAL INFILE '/Users/Carlos/Documents/download.txt' into table download";
      String sql35 = "LOAD DATA LOCAL INFILE '/Users/Carlos/Documents/song.txt' into table song";
      stmt.executeUpdate(sql33);
      stmt.executeUpdate(sql34);
      stmt.executeUpdate(sql35);

//*****************************************

//Output the SSNs and names of all customers who downloaded at least 
//one song with download time larger than 20.

      System.out.println("First Query......");
      String sql1 = "SELECT ssn, cname FROM customer c WHERE EXISTS (SELECT NULL FROM download d WHERE d.ssn = c.ssn AND d.time>20)";
      ResultSet rs1 = stmt.executeQuery(sql1);
      while(rs1.next()){
         int ssn  = rs1.getInt("ssn");
         String cname = rs1.getString("cname");

         //Display values
         System.out.print("SSN: " + ssn);
         System.out.println(", Name: " + cname);
      }

//*****************************************

//Output the SSNs and names of all customers such that all the songs downloaded 
//by the customer have the same price. 
//Note the songs downloaded by different customers may have different prices.

      System.out.println("Second Query......");
      //String sql2 = "SELECT c.ssn, c.cname FROM customer AS c INNER JOIN download AS d ON d.ssn=c.ssn INNER JOIN song AS s ON d.sid=s.sid WHERE s.price = 6";
      String sql2 = "SELECT DISTINCT t1.ssn, t1.cname  "+
         	    "FROM((SELECT cname, ssn, sid, price "+
			  "FROM download NATURAL JOIN customer NATURAL JOIN song) as t1, "+
   			 "(SELECT cname, ssn, sid, price "+
			  "FROM download NATURAL JOIN customer NATURAL JOIN song) as t2) "+
		    "WHERE t1.ssn = t2.ssn "+
                          "AND t1.ssn NOT IN (t1.sid != t2.sid "+
			         	     "AND t1.price != t2.price) "; 	
 
      ResultSet rs2 = stmt.executeQuery(sql2);

       while(rs2.next()){
         int ssn  = rs2.getInt("ssn");
         String cname = rs2.getString("cname");

         //Display values
         System.out.print("SSN: " + ssn);
         System.out.println(", NAME: " + cname);
      }

//*****************************************

//Output for each song, find the number of different professions of people that have downloaded that song. 
//Output the song’s name (SNAME) and the number of professions.

 System.out.println("Third Query......");
      String sql3 = "SELECT sname, count(distinct profession) as ct FROM song as s INNER JOIN download as d ON s.sid=d.sid INNER JOIN customer as c ON c.ssn=d.ssn GROUP BY sname";

      ResultSet rs3 = stmt.executeQuery(sql3);

       while(rs3.next()){
         String sname  = rs3.getString("sname");
         int ct = rs3.getInt("ct");

         //Display values
         System.out.print("Sname: " + sname);
         System.out.println(", Number: " + ct);
      }

//*****************************************
      
      String sql4 = "DELETE FROM song WHERE singer = 'John Denver'";
      stmt.executeUpdate(sql4);  
/*
      sql4 = "SELECT sid, sname, singer, price FROM song";
      ResultSet rs4 = stmt.executeQuery(sql4);

      while(rs4.next()){
         //Retrieve by column name
         int sid  = rs4.getInt("sid");
         String sname = rs4.getString("sname");
         String singer = rs4.getString("singer");
         int price = rs4.getInt("price");

         //Display values
         System.out.print("ID: " + sid);
         System.out.print(", Name: " + sname);
         System.out.print(", Singer: " + singer);
         System.out.println(", Price: " + price);
      }
*/
//*****************************************

      String sql5 = "INSERT INTO customer VALUES (217550501, 'Jack Cheng', 'male', 40, 'Showbiz')";
      stmt.executeUpdate(sql5);
      sql5 = "INSERT INTO customer VALUES (217550502, 'Monica', 'female', 23, 'Teacher')";
      stmt.executeUpdate(sql5);
/*

      sql5 = "SELECT ssn, cname, gender, age, profession FROM customer";
      ResultSet rs5 = stmt.executeQuery(sql5);

      while(rs5.next()){
         //Retrieve by column name
         int ssn  = rs5.getInt("ssn");
         String cname = rs5.getString("cname");
         String gender = rs5.getString("gender");
         int age = rs5.getInt("age");
         String profession = rs5.getString("profession");

         //Display values
         System.out.print("ID: " + ssn);
         System.out.print(", Name: " + cname);
         System.out.print(", Gender: " + gender);
         System.out.print(", Age: " + age);
         System.out.println(", Profession: " + profession);
      }
*/
//*****************************************

//Increase by $2 the price of each song whose current price is lower than 5. Update the song table.

      String sql6 = "UPDATE song SET price = price +2 WHERE price < 5";
      stmt.executeUpdate(sql6);
/*     
      sql6 = "SELECT sid, sname, singer, price FROM song";
      ResultSet rs6 = stmt.executeQuery(sql6);

      while(rs6.next()){
         //Retrieve by column name
         int sid  = rs6.getInt("sid");
         String sname = rs6.getString("sname");
         String singer = rs6.getString("singer");
         int price = rs6.getInt("price");

         //Display values
         System.out.print("ID: " + sid);
         System.out.print(", Name: " + sname);
         System.out.print(", Singer: " + singer);
         System.out.println(", Price: " + price);
      }
 */
//****************************************

      String sql7 = "DELETE FROM download WHERE time < 10";
      stmt.executeUpdate(sql7);
/*
      sql7 = "SELECT ssn, sid, time  FROM download";
      ResultSet rs7 = stmt.executeQuery(sql7);

      while(rs7.next()){
         //Retrieve by column name
         int ssn  = rs7.getInt("ssn");
         int sid = rs7.getInt("sid");
         int time = rs7.getInt("time");

         //Display values
         System.out.print("SSN: " + ssn);
         System.out.print(", ID: " + sid);
         System.out.println(", Time: " + time);
      }
*/
//*****************************************

      String sql8 = "SELECT sid, sname, singer, price FROM song";
      ResultSet rs8 = stmt.executeQuery(sql8);

      System.out.println("FINAL SONG TABLE");
      
      while (rs8.next()){
         int sid  = rs8.getInt("sid");
         String sname = rs8.getString("sname");
         String singer = rs8.getString("singer");
         int price = rs8.getInt("price");

         //Display values
         System.out.print("ID: " + sid);
         System.out.print(", Name: " + sname);
         System.out.print(", Singer: " + singer);
         System.out.println(", Price: " + price);
      }
//*****************************************
      String sql9 = "SELECT ssn, sid, time  FROM download";
      ResultSet rs9 = stmt.executeQuery(sql9);
      System.out.println("FINAL DOWNLOAD TABLE");

      while(rs9.next()){
         int ssn  = rs9.getInt("ssn");
         int sid = rs9.getInt("sid");
         int time = rs9.getInt("time");

         //Display values
         System.out.print("SSN: " + ssn);
         System.out.print(", ID: " + sid);
         System.out.println(", Time: " + time);
      }
//*****************************************
      String sql10 = "SELECT ssn, cname, gender, age, profession FROM customer";
      ResultSet rs10 = stmt.executeQuery(sql10);
      System.out.println("FINAL CUSTOMER TABLE");

      while(rs10.next()){
         int ssn  = rs10.getInt("ssn");
         String cname = rs10.getString("cname");
         String gender = rs10.getString("gender");
         int age = rs10.getInt("age");
         String profession = rs10.getString("profession");

         //Display values
         System.out.print("ID: " + ssn);
         System.out.print(", Name: " + cname);
         System.out.print(", Gender: " + gender);
         System.out.print(", Age: " + age);
         System.out.println(", Profession: " + profession);
      }



      rs1.close();
      rs2.close();
      rs3.close();
      //rs4.close();
      //rs5.close();
      //rs6.close();
      //rs7.close();    
      rs8.close();
      rs9.close();
      rs10.close();
      
      String sql20 = "DROP TABLE customer ";
      String sql21 = "DROP TABLE download ";
      String sql22 = "DROP TABLE song ";

      stmt.executeUpdate(sql20);
      System.out.println("CUSTOMER Table  deleted in given database...");
      stmt.executeUpdate(sql21);
      System.out.println("DOWNLOAD Table  deleted in given database...");
      stmt.executeUpdate(sql22);
      System.out.println("SONG Table  deleted in given database...");

   }
   catch(SQLException se)
   {
      //Handle errors for JDBC
      se.printStackTrace();
   }
   
   catch(Exception e)
   {
      //Handle errors for Class.forName
      e.printStackTrace();
   }
   /*
   finally
   {
      //finally block used to close resources
      try
      {
         if(stmt!=null)
            conn.close();
      }
      catch(SQLException se)
      {
      }// do nothing
      try
      {
         if(conn!=null)
            conn.close();
      }
      
      catch(SQLException se)
      {
         se.printStackTrace();
      }//end finally try
   }
   }//end try
*/
   System.out.println("Goodbye!");
}//end main
}//end JDBCExample
