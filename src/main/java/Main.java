import java.sql.*;
import java.util.Scanner;

public class Main {
  static String url = "jdbc:postgresql://localhost:5432/comp3005Q1";
  static String user = "postgres";
  static String password = "postgres";
  static Connection connection = null;
  
  public static void main(String[] args) {
    try {
      Class.forName("org.postgresql.Driver"); // Load the PostgreSQL driver else it won't work
      connection = DriverManager.getConnection(url, user, password);
      System.out.println("Connected to database");
      
      getAllStudents();
      
      Scanner scan = new Scanner(System.in);
      System.out.println("ADDING A STUDENT: ");
      System.out.println("Enter the first name of the student: ");
      String fName = scan.nextLine();
      System.out.println("Enter the last name of the student: ");
      String lName = scan.nextLine();
      System.out.println("Enter the email of the student: ");
      String eMail = scan.nextLine();
      System.out.println("Enter the enrollment date of the student (yyyy-mm-dd): ");
      Date eDate = Date.valueOf(scan.nextLine());
      addStudent(fName, lName, eMail, eDate);
      getAllStudents();
      
      System.out.println("UPDATING AN EMAIL: ");
      System.out.println("Enter the student_id of the student to update: ");
      Integer studentID = scan.nextInt();
      scan.nextLine(); // Consume the remaining newline character
      System.out.println("Enter the new email of the student: ");
      String newEmail = scan.nextLine();
      updateStudentEmail(studentID, newEmail);
      getAllStudents();
      
      System.out.println("DELETING A STUDENT: ");
      System.out.println("Enter the student_id of the student to delete: ");
      Integer studentID2 = scan.nextInt();
      scan.nextLine(); // Consume the remaining newline character
      deleteStudent(studentID2);
      getAllStudents();
      
      scan.close();
      connection.close();
    } catch (Exception e) {
      System.out.println("Exception Occured");
    }
  }
  
  public static void getAllStudents() {
    System.out.println("Call to getAllStudents()");
    try {
      Statement s = connection.createStatement();
      s.executeQuery("SELECT * FROM students ORDER BY student_id");
      ResultSet rSet = s.getResultSet();
      while (rSet.next()) {
        String result = rSet.getInt("student_id") + "\t" +
        rSet.getString("first_name") + "\t" +
        rSet.getString("last_name") + "\t" +
        rSet.getString("email") + "\t" +
        rSet.getString("enrollment_date");
        System.out.println(result);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public static void addStudent(String first_name, String last_name, String email, Date enrollment_date) {
    System.out.println("Call to addStudent(...)");
    String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, first_name);
      preparedStatement.setString(2, last_name);
      preparedStatement.setString(3, email);
      preparedStatement.setDate(4, enrollment_date);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public static void updateStudentEmail(Integer student_id, String new_email) {
    System.out.println("Call to updateStudentEmail(...)");
    String sql = "UPDATE students SET email = ? WHERE student_id = ?";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, new_email);
      preparedStatement.setInt(2, student_id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public static void deleteStudent(Integer student_id) {
    System.out.println("Call to deleteStudent(...)");
    String sql = "DELETE FROM students WHERE student_id = ?";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, student_id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
