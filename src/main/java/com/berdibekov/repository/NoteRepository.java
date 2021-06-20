package com.berdibekov.repository;

import com.berdibekov.domain.Note;
import com.berdibekov.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NoteRepository {

    @Value("${db.host}")
    private String dataBaseURL;

    private String insertNotesTagsSql = "INSERT INTO notes_tags (note_id, tag_id) " +
                                        "SELECT ?, ? " +
                                        "WHERE NOT EXISTS(" +
                                        "       SELECT note_id, tag_id " +
                                        "       FROM notes_tags " +
                                        "       WHERE note_id = ? AND tag_id = ?);";

    private String insertTagSql = "INSERT INTO tags (tag_id) " +
                                  "     SELECT ? " +
                                  "     WHERE NOT EXISTS (SELECT tag_id FROM tags WHERE tag_id = ?);";

    private String findTagsSql = "select t.* from tags t " +
                                 "left join notes_tags on t.tag_id = notes_tags.tag_id " +
                                 "WHERE note_id = ?;";

    public NoteRepository() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public Note save(Note note) throws SQLException {
        String sql = "INSERT INTO notes ( name , date_time ,text) VALUES(?,?,?) ;";
        String insertedNoteIdQuery = "SELECT max(note_id) from notes ;";

        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement insertNote = connection.prepareStatement(sql);
             PreparedStatement insertTag = connection.prepareStatement(insertTagSql);
             PreparedStatement insertNotesTags = connection.prepareStatement(insertNotesTagsSql);
             Statement getNoteId = connection.createStatement()) {
            insertNote.setString(1, note.getNoteName());
            insertNote.setTimestamp(2, new Timestamp(note.getDateTime().getTime()));
            insertNote.setString(3, note.getText());
            insertNote.execute();
            ResultSet resultSet = getNoteId.executeQuery(insertedNoteIdQuery);
            if (resultSet.next()) {
                int noteId = resultSet.getInt(1);
                note.setId(noteId);
            }
            insertNoteTags(note, insertNotesTags, insertTag);
            return note;
        }
    }

    public void deleteById(Long noteId) {
        String deleteNotesTagsSql = "delete from notes_tags WHERE note_id = ?;";
        String deleteNoteSql = "delete from notes WHERE note_id = ?;";

        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement deleteNotesTags = connection.prepareStatement(deleteNotesTagsSql);
             PreparedStatement deleteNote = connection.prepareStatement(deleteNoteSql)) {
            deleteNotesTags.setLong(1, noteId);
            deleteNote.setLong(1, noteId);
            deleteNote.execute();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public void update(long noteId, Note note) throws SQLException {
        String updateNoteSql = "UPDATE notes SET name = ?, text = ?,date_time = ? WHERE note_id = 7;";
        String deleteNotesTagsSql = "delete from notes_tags WHERE note_id = ?;";

        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement updateNote = connection.prepareStatement(updateNoteSql);
             PreparedStatement deleteNotesTags = connection.prepareStatement(deleteNotesTagsSql);
             PreparedStatement insertNotesTags = connection.prepareStatement(insertNotesTagsSql);
             PreparedStatement insertTag = connection.prepareStatement(insertTagSql)) {
            updateNote.setString(1, note.getNoteName());
            updateNote.setString(2, note.getText());
            updateNote.setTimestamp(3, new Timestamp(note.getDateTime().getTime()));
            updateNote.execute();
            deleteNotesTags.setLong(1, noteId);
            deleteNotesTags.execute();
            note.setId(noteId);
            insertNoteTags(note, insertNotesTags, insertTag);
        }
    }

    private void insertNoteTags(Note note, PreparedStatement insertNotesTags, PreparedStatement insertTag) throws SQLException {
        for (String hashTag : note.getHashTags()) {
            insertTag.setString(1, hashTag);
            insertTag.setString(2, hashTag);
            insertTag.execute();
            insertNotesTags.setLong(1, note.getId());
            insertNotesTags.setString(2, hashTag);
            insertNotesTags.setLong(3, note.getId());
            insertNotesTags.setString(4, hashTag);
            insertNotesTags.execute();
        }
    }

    public boolean isExist(Long noteId) throws SQLException {
        String sql = "select * from notes where note_id = ?";
        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement selectBySubstring = connection.prepareStatement(sql)) {
            selectBySubstring.setLong(1, noteId);
            selectBySubstring.executeQuery();
            if (selectBySubstring.executeQuery().next()) {
                return true;
            }
            return false;
        }
    }

    public List<Note> findAllBySubString(String subString) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "select * from notes where text LIKE ?;";
        String sqlFindTags = "select t.* from tags t  " +
                             "left join notes_tags on t.tag_id = notes_tags.tag_id " +
                             "WHERE note_id = ?;";
        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement selectBySubstring = connection.prepareStatement(sql);
             PreparedStatement findTags = connection.prepareStatement(sqlFindTags)) {
            selectBySubstring.setString(1, "%" + subString + "%");
            ResultSet resultSet = selectBySubstring.executeQuery();
            mapNotes(notes, findTags, resultSet);
            return notes;
        }
    }

    public List<Note> findAllByHashTag(String hashTag) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "select n.* FROM NOTES n " +
                     "LEFT JOIN notes_tags ON n.note_id = notes_tags.note_id " +
                     "WHERE tag_id = ?";
        String sqlFindTags = "select t.* from tags t  " +
                             "left join notes_tags on t.tag_id = notes_tags.tag_id " +
                             "WHERE note_id = ?;";
        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement findByTag = connection.prepareStatement(sql);
             PreparedStatement findTags = connection.prepareStatement(sqlFindTags)) {
            findByTag.setString(1, hashTag);
            ResultSet resultSet = findByTag.executeQuery();
            mapNotes(notes, findTags, resultSet);
            return notes;
        }
    }

    public List<Note> findAllByHashTagAndSubString(String subString, String hashTag) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "select n.* FROM NOTES n " +
                     "LEFT JOIN notes_tags ON n.note_id = notes_tags.note_id " +
                     "WHERE tag_id = ? AND n.note_id IN (select note_id from notes where text LIKE ?);";

        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             PreparedStatement findByTagAndSubString = connection.prepareStatement(sql);
             PreparedStatement findTags = connection.prepareStatement(findTagsSql)) {
            findByTagAndSubString.setString(1, hashTag);
            findByTagAndSubString.setString(2, "%" + subString + "%");
            ResultSet resultSet = findByTagAndSubString.executeQuery();
            mapNotes(notes, findTags, resultSet);
            return notes;
        }
    }

    public List<Note> findAll() throws SQLException {
        String sql = "select * from notes;";
        List<Note> notes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dataBaseURL);
             ResultSet noteSet = connection.createStatement().executeQuery(sql);
             PreparedStatement findTags = connection.prepareStatement(findTagsSql)) {
            mapNotes(notes, findTags, noteSet);
        }
        return notes;
    }

    private void mapNotes(List<Note> notes, PreparedStatement findTags, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Note note = new Note();
            List<String> hashTags = new ArrayList<>();
            note.setHashTags(hashTags);
            note.setId(resultSet.getLong("note_id"));
            note.setDateTime(resultSet.getTimestamp("date_time"));
            note.setText(resultSet.getString("text"));
            note.setNoteName(resultSet.getString("name"));
            findTags.setLong(1, note.getId());
            ResultSet tagResultSet = findTags.executeQuery();
            while (tagResultSet.next()) {
                hashTags.add(tagResultSet.getString(1));
            }
            notes.add(note);
        }
    }
}
