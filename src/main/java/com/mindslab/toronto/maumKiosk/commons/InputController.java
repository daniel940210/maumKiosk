package com.mindslab.toronto.maumKiosk.commons;

import com.mindslab.toronto.maumKiosk.runner.api_engine.faceTracking.FaceTracking_Service;
import com.mindslab.toronto.maumKiosk.test.FTtoFR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.sql.*;

import java.util.Date;

@Slf4j
@Controller
public class InputController {
    @Autowired
    private FaceTracking_Service ft_service;
    
    @Autowired
    private FTtoFR fTtoFR;
    
    @RequestMapping("/")
    public String input (Model model) {
        Connection testConnection = getConnectionToDB();
        String greeting = "";
        if(testConnection == null) {
            greeting = "No Connection!";
        }
        else {
            greeting = GetGreeting(testConnection);
        }
        model.addAttribute("datetime", new Date());
        model.addAttribute("Greeting", greeting);
        return "view/input";
    }
    
    public String GetGreeting(Connection conn) {
        Statement stmt = null;
        String query = "SELECT TOP 1 * FROM GREETINGS";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getString("Greeting");
        } catch (SQLException e) {
            return "";
        }
    }
    
    public Connection getConnectionToDB() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:sqlserver://SWHANG-MINDSLAB\\SQLEXPRESS",
                "maumKiosk",
                "1234"
            );
            return conn;
        }
        catch (Exception e) {
            return null;
        }
    }
}
