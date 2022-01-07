package ua.olharudenko.libraryapp.dao;

import ua.olharudenko.libraryapp.enums.Role;
import ua.olharudenko.libraryapp.models.User;
import ua.olharudenko.libraryapp.utils.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements ModelDAO<User> {

    @Override
    public Optional<User> get(long id) throws SQLException {
        User user = null;
        Connection connection = null;
        PreparedStatement pstatement = null;
        ResultSet resultSet = null;
        String sql = "select * from users  where id = ?";
        try {
            connection = DataBaseConnection.getInstance().getConn();
            pstatement = connection.prepareStatement(sql);
            pstatement.setLong(1, id);
            if (pstatement.execute()) {
                resultSet = pstatement.executeQuery();
                while (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setRole(Role.valueOf(resultSet.getString("role")));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAdress(resultSet.getString("adress"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("USER NOT FOUND BY ID", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                pstatement.close();
            } catch (SQLException e) {
                throw new SQLException("get: ResultSet or PreparedStatement didn't close", e);
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> allUsers = new ArrayList<User>();
        Connection connection = null;
        PreparedStatement pstatement = null;
        ResultSet resultSet = null;
        String sql = "select * from users";
        try {
            connection = DataBaseConnection.getInstance().getConn();
            pstatement = connection.prepareStatement(sql);
            resultSet = pstatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setAdress(resultSet.getString("adress"));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new SQLException("USERS NOT FOUND", e);
        } finally {
            try {
                resultSet.close();
                pstatement.close();
            } catch (SQLException e) {
                throw new SQLException("findAll(): ResultSet or PreparedStatement didn't close", e);
            }
        }
        if (allUsers.size() == 0) {
            throw new SQLException("table USERS is empty");
        }
        return allUsers;
    }

    @Override
    public User save(User user) {
        Connection connection = null;
        PreparedStatement pstatement = null;
        String sql = "insert into users(first_name, last_name, role, email, password, phone, adress) values (?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = DataBaseConnection.getInstance().getConn();
            pstatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstatement.setString(1, user.getFirstName());
            pstatement.setString(2, user.getLastName());
            pstatement.setString(3, user.getRole().toString());
            pstatement.setString(4, user.getEmail());
            pstatement.setString(5, user.getPassword());
            pstatement.setString(6, user.getPhone());
            pstatement.setString(7, user.getAdress());

            if (pstatement.executeUpdate() == 0) {
                throw new SQLException("Adding user to database failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Adding user to database failed, no ID obtained");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement pstatement = null;
        String sql = "UPDATE users SET first_name=?, last_name=?, role=?, email=?, password=?, phone=?, adress=? WHERE id =?";
        try {
            connection = DataBaseConnection.getInstance().getConn();
            pstatement = connection.prepareStatement(sql);
            if (get(user.getId()) != null) {
                pstatement.setLong(8, user.getId());
                pstatement.setString(1, user.getFirstName());
                pstatement.setString(2, user.getLastName());
                pstatement.setString(3, user.getRole().toString());
                pstatement.setString(4, user.getEmail());
                pstatement.setString(5, user.getPassword());
                pstatement.setString(6, user.getPhone());
                pstatement.setString(7, user.getAdress());
                pstatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("USER DIDN'T UPDATE");
        } finally {
            try {
                pstatement.close();
            } catch (SQLException e) {
                throw new SQLException("update: PreparedStatement didn't close", e);
            }
        }
    }

    @Override
    public void delete(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement pstatement = null;
        String sql = "delete from users where id = ?";
        try {
            connection = DataBaseConnection.getInstance().getConn();
            pstatement = connection.prepareStatement(sql);
            if (get(user.getId()) != null) {
                pstatement.setLong(1, user.getId());
                pstatement.executeUpdate();
            } else {
                throw new SQLException("USER DIDN'T DELETE");
            }
        } catch (SQLException e) {
            throw new SQLException("USER DIDN'T DELETE");
        } finally {
            try {
                pstatement.close();
            } catch (SQLException e) {
                throw new SQLException("delete: PreparedStatement didn't close", e);
            }
        }
    }

    public Optional<User> findUserByEmail(String email) throws SQLException {
        User user = null;
        Connection connection = null;
        PreparedStatement pstatement = null;
        ResultSet resultSet = null;
        String sql = "select * from users  where email = ?";
        System.out.println("EMAIL " + email);
        try {
            connection = DataBaseConnection.getInstance().getConn();
            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, email);
            if (pstatement.execute()) {
                resultSet = pstatement.executeQuery();
                while (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setRole(Role.valueOf(resultSet.getString("role")));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAdress(resultSet.getString("adress"));
                }
                System.out.println("USERR " + user.toString());
            }
        } catch (SQLException e) {
            throw new SQLException("USER NOT FOUND BY EMAIL", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
//                pstatement.close();
            } catch (SQLException e) {
                throw new SQLException("get: ResultSet or PreparedStatement didn't close", e);
            }
        }
        return Optional.ofNullable(user);
    }
}
