package com.heallink.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/PatientServlet")
public class PatientServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    String jdbcURL = "jdbc:mysql://localhost:3306/heallink";
    String dbUser = "root";
    String dbPassword = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if (action == null) {
            out.println("<h3>Invalid Request</h3>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            switch (action) {
                case "register":
                    registerPatient(request, response, con);
                    break;

                case "login":
                    loginPatient(request, response, con);
                    break;

                case "bookAppointment":
                    bookAppointment(request, response, con);
                    break;

                default:
                    out.println("<h3>Invalid Action</h3>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<h3 style='color:red'>Server Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }

    // ------------------------ Register Patient ------------------------
    private void registerPatient(HttpServletRequest request, HttpServletResponse response, Connection con)
            throws Exception {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");

        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO patients(name, email, password, gender, phone) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, gender);
        ps.setString(5, phone);

        int rows = ps.executeUpdate();
        PrintWriter out = response.getWriter();

        if (rows > 0) {
            out.println("<script>alert('Registration Successful!');window.location='login.jsp';</script>");
        } else {
            out.println("<script>alert('Error! Try Again.');window.location='register.jsp';</script>");
        }
    }

    // ------------------------ Login Patient ------------------------
    private void loginPatient(HttpServletRequest request, HttpServletResponse response, Connection con)
            throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM patients WHERE email=? AND password=?");
        ps.setString(1, email);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        PrintWriter out = response.getWriter();

        if (rs.next()) {
            HttpSession session = request.getSession();
            session.setAttribute("patientName", rs.getString("name"));
            session.setAttribute("patientEmail", rs.getString("email"));
            response.sendRedirect("dashboard.jsp");
        } else {
            out.println("<script>alert('Invalid Credentials!');window.location='login.jsp';</script>");
        }
    }

    // ------------------------ Book Appointment ------------------------
    private void bookAppointment(HttpServletRequest request, HttpServletResponse response, Connection con)
            throws Exception {
        String doctorName = request.getParameter("doctorName");
        String appointmentDate = request.getParameter("appointmentDate");
        String issue = request.getParameter("issue");
        String patientEmail = (String) request.getSession().getAttribute("patientEmail");

        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments(doctor_name, appointment_date, issue, patient_email) VALUES (?, ?, ?, ?)");
        ps.setString(1, doctorName);
        ps.setString(2, appointmentDate);
        ps.setString(3, issue);
        ps.setString(4, patientEmail);

        int rows = ps.executeUpdate();
        PrintWriter out = response.getWriter();

        if (rows > 0) {
            out.println("<script>alert('Appointment Booked Successfully!');window.location='dashboard.jsp';</script>");
        } else {
            out.println("<script>alert('Failed to Book Appointment!');window.location='appointment.jsp';</script>");
        }
    }
}
