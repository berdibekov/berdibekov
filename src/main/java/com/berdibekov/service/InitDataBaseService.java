package com.berdibekov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class InitDataBaseService {

    private String dataBaseURL;

    @Autowired
    public InitDataBaseService(@Value("${db.host}") String dataBaseURL) {
        try {
            this.dataBaseURL = dataBaseURL;
            Class.forName("org.h2.Driver");
            String sql = lodeInitScript();
            createDatabaseIfNotExists(sql);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void createDatabaseIfNotExists(String sql) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             Statement init = connection.createStatement();) {
            init.execute(sql);
        }
    }

    private String lodeInitScript() {
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
