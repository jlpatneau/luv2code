package com.luv2code.web.jdbc;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDbUtil {

    private DataSource dataSource;

    public StudentDbUtil(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    public List<Student> getStudents() throws Exception {

        List<Student> students = new ArrayList<>();

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            //get a connection
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_student_tracker?"
                    + "user=webstudent&password=webstudent");
            //myConn = dataSource.getConnection();
            //create sql
            String sql = "select * from student order by last_name";
            myStmt = myConn.createStatement();

            //execute sql
            myRs = myStmt.executeQuery(sql);

            //process results
            while (myRs.next()) {
                //retrieve data from result set row
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                //create new student object
                Student tempStudent = new Student(id, firstName, lastName, email);

                //add to list of students
                students.add(tempStudent);
            }

            return students;

        } finally {
            //close JDBC objects
            close(myConn, myStmt, myRs);
        }
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
        try {
            if(myRs != null) {
                myRs.close();
            }
            if(myStmt != null) {
                myStmt.close();
            }
            if(myConn != null) {
                myConn.close();
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void addStudent(Student theStudent) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;
        //ResultSet myRs = null;

        try {
            //get a connection
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_student_tracker?"
                    + "user=webstudent&password=webstudent");
            //myConn = dataSource.getConnection();
            //create sql
            String sql = "insert into student "
                    + "(first_name, last_name, email) "
                    + "values (?, ?, ?)";

            myStmt = myConn.prepareStatement(sql);

            //set param values
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());

            //execute sql insert
            myStmt.execute();

        } finally {
            //close JDBC objects
            close(myConn, myStmt, null);
        }
    }

    public Student getStudent(String theStudentId) throws Exception {

        Student theStudent = null;

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int studentId;

        try {
            //convert student id to int
            studentId = Integer.parseInt(theStudentId);

            //db connection?
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_student_tracker?"
                    + "user=webstudent&password=webstudent");

            //create sql
            String sql = "select * from student where id=?";

            //create prepared statemtn
            myStmt = myConn.prepareStatement(sql);

            //set params
            myStmt.setInt(1, studentId);

            //execute statement
            myRs = myStmt.executeQuery();
            //retrieve data
            if (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                theStudent = new Student(studentId, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student id: "
                        + studentId);
            }

            return theStudent;

        } finally {
            //clean up JDBC
            close(myConn, myStmt, myRs);
        }


    }

    public void updateStudent(Student theStudent) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_student_tracker?"
                    + "user=webstudent&password=webstudent");

            //create sql
            String sql = "update student "
                    + "set first_name=?, last_name=?, email=? "
                    + "where id=?";

            //create prepare statement
            myStmt = myConn.prepareStatement(sql);

            //set params (?s)
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setInt(4, theStudent.getId());

            //execute SQL
            myStmt.execute();

        } finally {
            close(myConn, myStmt, null);
        }

    }

    public void deleteStudent(String theStudentId) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            //convert student id to int
            int studentId = Integer.parseInt(theStudentId);
            //connect to db
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/web_student_tracker?"
                    + "user=webstudent&password=webstudent");

            //create sql
            String sql = "delete from student where id=?";

            //prepare statement
            myStmt = myConn.prepareStatement(sql);

            //set params
            myStmt.setInt(1, studentId);

            //exec sql
            myStmt.execute();

        } finally {
            close(myConn, myStmt, null);
        }

    }
}
