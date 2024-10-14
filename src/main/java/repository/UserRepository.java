package repository;


import connection.ConnectionFactory;
import domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    public static List<User> findBYName(String name) {

        System.out.println("----- Finding by name ---------");

        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByName(conn, name);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getInt("id_user"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .build();

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error finding by name");
        }

        return users;

    }

    public static Optional<User> findById(Integer id) {

        System.out.println("Findind by name");


        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindById(conn, id);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return Optional.empty();

            return Optional.of(User.builder()
                    .id(rs.getInt("id_user"))
                    .name(rs.getString("name"))
                    .phone(rs.getString("phone"))
                    .email(rs.getString("email"))
                    .build());

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error finding by name");
        }

        return Optional.empty();

    }

    public static void delete(Integer id) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementDelete(conn, id)) {
            ps.execute();
            System.out.printf("User deleted %d from the database", id);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void save(User user) {
        System.out.println("Saving user " + user.getName());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementSave(conn, user)) {
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(User user) {
        System.out.println("Updating user");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPrepareStatementUpdate(conn, user)) {
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error while trying update user " + user.getId());
        }
    }

    public static List<User> allUserWithId() {

        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = returnAllIdWithNameUser(conn);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getInt("id_user"))
                        .name(rs.getString("name"))
                        .build();

                users.add(user);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;

    }


    private static PreparedStatement createPreparedStatementFindByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT * FROM \"user\" WHERE name like ? ";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, String.format("%%%s%%", name));

        return ps;
    }

    private static PreparedStatement createPreparedStatementFindById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM \"user\" WHERE id_user = ? ";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, id);

        return ps;
    }

    private static PreparedStatement createPrepareStatementDelete(Connection conn, Integer id) throws SQLException {

        String sql = "DELETE FROM \"user\" WHERE id_user = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        return ps;

    }

    private static PreparedStatement createPrepareStatementSave(Connection conn, User user) throws SQLException {
        String sql = "INSERT INTO \"user\" (name, phone, email) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPhone());
        ps.setString(3, user.getEmail());

        return ps;
    }

    private static PreparedStatement createPrepareStatementUpdate(Connection conn, User user) throws SQLException {
        String sql = "UPDATE \"user\" SET name = ?, phone = ?, email = ? WHERE id_user = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPhone());
        ps.setString(3, user.getEmail());
        ps.setInt(4, user.getId());

        return ps;
    }


    private static PreparedStatement returnAllIdWithNameUser(Connection connection) throws SQLException {
        String sql = "SELECT id_user, name FROM \"user\"; ";
        return connection.prepareStatement(sql);
    }


}
