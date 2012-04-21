package trunk;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Bogdanov Kirill
 * Date: 23.03.12
 * Time: 22:33
 */
public class SettingsManager {
    private static Connection con;

    SettingsManager() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            final String url = "jdbc:mysql://localhost:3306/user_settings";
            con = DriverManager.getConnection(url, "root", "root");
        } catch (ClassNotFoundException e) {
            throw new Exception(e);
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    public void createSettings(final long userId, final String group) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE user_" + userId + " (id INT, day VARCHAR(15), start_time VARCHAR(10)," +
                    " end_time VARCHAR(10), parity VARCHAR(5), place TEXT, subject TEXT, type VARCHAR(10)," +
                    " teacher TEXT, skip VARCHAR(10), wake_time VARCHAR(10), message TEXT, turn INT, primary key(id))");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ifmo_schedule", "root", "root");
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM g_" + group);
            ResultSet rs = pstmt.executeQuery();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_settings", "root", "root");
            int id = 1;
            while (rs.next()) {
                pstmt = con.prepareStatement("INSERT INTO user_" + userId + " (id, day, start_time, end_time, parity," +
                        " place, subject, type, teacher, skip, wake_time, message, turn) VALUES (?, ?, ?, ?, ?, ?, ?," +
                        " ?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, id);
                pstmt.setString(2, rs.getString(1));
                pstmt.setString(3, rs.getString(2));
                pstmt.setString(4, rs.getString(3));
                pstmt.setString(5, rs.getString(4));
                pstmt.setString(6, rs.getString(5));
                pstmt.setString(7, rs.getString(6));
                pstmt.setString(8, rs.getString(7));
                pstmt.setString(9, rs.getString(8));
                pstmt.setString(10, "no");
                pstmt.setString(11, rs.getString(2));
                pstmt.setString(12, "");
                pstmt.setInt(14-1, 1);
                pstmt.execute();
                id++;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public Collection<String> getRings(final long userId, final String day, final String parity) throws SQLException {
        List<String> rings = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT wake_time FROM user_" + userId + " WHERE day=? AND (parity=? OR " +
                    "parity=?) AND skip=?");
            stmt.setString(1, day);
            stmt.setString(2, parity);
            stmt.setString(3, "");
            stmt.setString(4, "no");
            rs = stmt.executeQuery();
            while (rs.next()) {
                rings.add(rs.getString(1));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return rings;
    }

    public void updateSettings(final long userId, final long id, final String skip, final String wake_time,
                               final String message, final int turn) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(
                    "UPDATE user_" + userId + " SET skip=?, wake_time=?, message=?, turn=? " +
                            "WHERE id=?");
            stmt.setString(1, skip);
            stmt.setString(2, wake_time);
            stmt.setString(3, message);
            stmt.setInt(4, turn);
            stmt.setLong(5, id);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public Collection<String> getSettings(final long userId, final long id) throws SQLException {
        List<String> settings = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT skip, wake_time, message, turn FROM user_" + userId +
                    " WHERE id=?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                settings.add(rs.getString(1));
                settings.add(rs.getString(2));
                settings.add(rs.getString(3));
                settings.add(rs.getInt(4) + "");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return settings;
    }

    public Collection<Collection<String>> getSchedule(final long userId, final String day, final String parity)
            throws SQLException {
        List<Collection<String>> schedule = new ArrayList<Collection<String>>();
        List<String> note = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT id, start_time, end_time, place, subject, type, teacher FROM user_" +
                    userId + " WHERE day=? AND (parity=? OR parity=?)");
            stmt.setString(1, day);
            stmt.setString(2, parity);
            stmt.setString(3, "");
            rs = stmt.executeQuery();
            while (rs.next()) {
                for (int i = 1; i < 8; i++) {
                    note.add(rs.getString(i));
                }
                schedule.add(note);
                note = new ArrayList<String>();
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return schedule;
    }
}
