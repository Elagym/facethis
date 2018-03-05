package com.zizi.facethis.repository;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Vince on 03-03-18.
 */
public final class DatabaseConnexion {

    private static DatabaseConnexion instance;
    private static final String HOST = "213.32.69.83";
    private static final String PORT = "3306";
    private static final String DATABASE = "ebotv3";
    private static final String LOGIN = "ebotv3";
    private static final String PWD = "csgotest";

    private static Connection conn;

    private DatabaseConnexion() {
        init();
    }

    public static DatabaseConnexion getInstance() {
        if (instance == null) {
            instance = new DatabaseConnexion();
        }
        return instance;
    }

    private void init() {
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, LOGIN, PWD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}
