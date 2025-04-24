/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.busreservation.busresevationsystem;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Login Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
        public void run() {
    try (
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
    ) {
        String requestType = reader.readLine();

        if ("REGISTER".equalsIgnoreCase(requestType)) {
            String firstName = reader.readLine();
            String lastName = reader.readLine();
            String username = reader.readLine();
            String email = reader.readLine();
            String password = reader.readLine();
            String weburl = reader.readLine();

            boolean registered = registerUser(firstName, lastName, username, email, password, weburl);
            writer.println(registered ? "SUCCESS" : "USER_EXISTS");

        } else if ("LOGIN".equalsIgnoreCase(requestType)) {
            String username = reader.readLine();
            String password = reader.readLine();

            boolean isAuthenticated = authenticateUser(username, password);
            writer.println(isAuthenticated ? "SUCCESS" : "FAIL");
            
        } else if("ADMINLOGIN".equalsIgnoreCase(requestType)){
            String username = reader.readLine();
            String password = reader.readLine();
            
            boolean isAuthenticated = authenticateAdminUser(username, password);
            writer.println(isAuthenticated? "SUCCESS":"FAIL");
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean registerUser(String firstName, String lastName, String username, String email, String password, String weburl) {
        String checkQuery = "SELECT * FROM users WHERE email = ? OR username = ?";
        String insertQuery = "INSERT INTO users (name, username, email, password, weburl) VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery)
        ) {
            checkStmt.setString(1, email);
            checkStmt.setString(2, username);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // User already exists
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, firstName + " " + lastName);
                insertStmt.setString(2, username);
                insertStmt.setString(3, email);
                insertStmt.setString(4, password);
                insertStmt.setString(5, weburl);
                return insertStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    private boolean authenticateAdminUser(String username, String password) {
    String query = "SELECT password FROM admin WHERE username = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String storedPassword = rs.getString("password");
            return storedPassword.equals(password); // Compare passwords (use hashing for security)
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}
