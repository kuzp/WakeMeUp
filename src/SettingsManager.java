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

    public void createSettings(final int userId, final String group) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE user_" + userId + " (day VARCHAR(15), start_time VARCHAR(10)," +
                    " end_time VARCHAR(10), parity VARCHAR(5), place TEXT, subject TEXT," +
                    " type VARCHAR(10), teacher TEXT, skip VARCHAR(10), wake_time VARCHAR(10))");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ifmo_schedule", "root", "root");
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM g_" + group);
            ResultSet rs = pstmt.executeQuery();
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_settings", "root", "root");
            while (rs.next()) {
                pstmt = con.prepareStatement("INSERT INTO user_" + userId + " (day, start_time, end_time, parity," +
                        " place, subject, type, teacher, skip, wake_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, rs.getString(1));
                pstmt.setString(2, rs.getString(2));
                pstmt.setString(3, rs.getString(3));
                pstmt.setString(4, rs.getString(4));
                pstmt.setString(5, rs.getString(5));
                pstmt.setString(6, rs.getString(6));
                pstmt.setString(7, rs.getString(7));
                pstmt.setString(8, rs.getString(8));
                pstmt.setString(9, "no");
                pstmt.setString(10, rs.getString(2));
                pstmt.execute();
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public Collection<String> getRings(final int userId, final String day, final String parity) throws SQLException {
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

    public void updateSettings(final int userId, final String day, final String parity, final String time,
                               final String skip, final String wake_time) throws SQLException {
          PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(
                    "UPDATE user_" + userId + " SET skip=?, wake_time=? " +
                    "WHERE day=? AND (parity=? OR parity=?) AND start_time=?");
            stmt.setString(1, skip);
            stmt.setString(2, wake_time);
            stmt.setString(3, day);
            stmt.setString(4, parity);
            stmt.setString(5, "");
            stmt.setString(6, time);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
