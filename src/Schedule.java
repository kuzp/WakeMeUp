import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Bogdanov Kirill
 * Date: 28.02.12
 * Time: 20:22
 */
public class Schedule {
    private static Connection con;

    Schedule() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            final String url = "jdbc:mysql://localhost:3306/ifmo_schedule";
            con = DriverManager.getConnection(url, "root", "root");
        } catch (ClassNotFoundException e) {
            throw new Exception(e);
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    public void createSchedule(final String group) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE TABLE g_" + group + " (day VARCHAR(15), start_time VARCHAR(10)," +
                    " end_time VARCHAR(10), parity VARCHAR(5), place TEXT, subject TEXT," +
                    " type VARCHAR(10), teacher TEXT)");
        } catch (SQLException ex) {
            stmt = con.createStatement();
            stmt.execute("DROP TABLE g_" + group);
            stmt.close();
            createSchedule(group);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public void saveRing(final String day, final String startTime, final String endTime, final String parity,
                         final String place, final String subject, final String type,
                         final String teacher, final String group) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(
                    "INSERT INTO g_" + group +
                            " (day, start_time, end_time, parity, place, subject, type, teacher) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, day);
            stmt.setString(2, startTime);
            stmt.setString(3, endTime);
            stmt.setString(4, parity);
            stmt.setString(5, place);
            stmt.setString(6, subject);
            stmt.setString(7, type);
            stmt.setString(8, teacher);
            stmt.execute();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public Collection<String> getRings(final String group, final String day, final String parity) throws SQLException {
        List<String> rings = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT start_time FROM g_" + group + " WHERE day=? AND (parity=? OR parity=?)");
            stmt.setString(1, day);
            stmt.setString(2, parity);
            stmt.setString(3, "");
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

    public Collection<Collection<String>> getSchedule(final String group, final String day, final String parity)
            throws SQLException {
        List<Collection<String>> schedule = new ArrayList<Collection<String>>();
        List<String> note = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT start_time, end_time, place, subject, type, teacher FROM g_" + group + " WHERE day=? AND (parity=? OR parity=?)");
            stmt.setString(1, day);
            stmt.setString(2, parity);
            stmt.setString(3, "");
            rs = stmt.executeQuery();
            while (rs.next()) {
                for (int i = 1; i < 7; i++) {
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
