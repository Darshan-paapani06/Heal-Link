<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>HealLink Dashboard</title>
    <style>
        body { font-family: Arial; background-color: #f0f6fa; }
        .container { width: 70%; margin: 50px auto; background: white; padding: 20px; border-radius: 12px; box-shadow: 0 0 10px #ccc; }
        h2 { color: #1a73e8; }
        button { padding: 10px 20px; border: none; background-color: #1a73e8; color: white; border-radius: 6px; cursor: pointer; }
        button:hover { background-color: #155cb0; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Welcome, <%= session.getAttribute("patientName") %> 👋</h2>
        <p>You are logged in to HealLink — your personal healthcare communication space.</p>

        <form action="PatientServlet" method="post">
            <input type="hidden" name="action" value="bookAppointment">
            <h3>Book an Appointment</h3>
            <label>Doctor Name:</label><br>
            <input type="text" name="doctorName" required><br><br>

            <label>Date:</label><br>
            <input type="date" name="appointmentDate" required><br><br>

            <label>Describe your issue:</label><br>
            <textarea name="issue" rows="4" cols="40" required></textarea><br><br>

            <button type="submit">Book Appointment</button>
        </form>
    </div>
</body>
</html>
