package com.berdibekov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.*;
import java.util.Objects;

@Component
public class InitDataBaseService {
    @Value("${db.host}")
    private String dataBaseURL;

    @Autowired
    public InitDataBaseService(){
        try {
            Class.forName("org.h2.Driver");
            String sql = lodeInitScript();
            createDatabaseIfNotExists(sql);
        } catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createDatabaseIfNotExists(String sql) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             Statement init = connection.createStatement();){
            init.execute(sql);
        }
    }

    private String lodeInitScript() throws FileNotFoundException {
        return "CREATE TABLE IF NOT EXISTS notes\n" +
                "(\n" +
                "    note_id INTEGER PRIMARY KEY auto_increment,\n" +
                "    data    DATE NOT NULL,\n" +
                "    name    VARCHAR,\n" +
                "    text    TEXT NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS tags\n" +
                "(\n" +
                "    tag_id VARCHAR PRIMARY KEY\n" +
                ");\n" +
                "\n" +
                "create TABLE IF NOT EXISTS notes_tags\n" +
                "(\n" +
                "    note_id INTEGER REFERENCES notes (note_id) ON DELETE CASCADE,\n" +
                "    tag_id  VARCHAR REFERENCES tags (tag_id) ON DELETE CASCADE,\n" +
                "    PRIMARY KEY (note_id, tag_id)\n" +
                ");";

    }
}
