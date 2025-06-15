package model.repositories;

import model.dto.user.DeleteUserDto;
import model.dto.user.UpdateUserDto;
import model.entities.Users;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserRepositoryImpl implements Repository<Users, Integer> {
    //private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Override
    public Users save(Users entity) {
        String sql = """
                INSERT INTO users (username,email,password,is_deleted,uuid,is_logged_in)
                VALUES (?,?,?,?,?,?);
                """;
        try (Connection con = DatabaseConfig.getDatabaseConnection()) {
            assert con != null;
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, entity.getUsername());
            pre.setString(2, entity.getEmail());
            pre.setString(3, entity.getPassword());
            pre.setBoolean(4, entity.is_deleted());
            pre.setString(5, entity.getUuid());
            pre.setBoolean(6, entity.is_logged_in());
            int rowAffected = pre.executeUpdate();
            return rowAffected > 0 ? entity : null;
        } catch (Exception e) {
            System.out.println("Error during inserting user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Users> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = DatabaseConfig.getDatabaseConnection()) {
            assert con != null;
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setInt(1, id);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                Users user = new Users();
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setUuid(rs.getString("uuid"));
                user.set_logged_in(rs.getBoolean("is_logged_in"));
                user.set_deleted(rs.getBoolean("is_deleted"));
                return Optional.of(user);
            }
        } catch (Exception e) {
            System.out.println("Error during finding user: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Users findByUserUuid(String uuid) {
        String sql = "SELECT * FROM users WHERE uuid = ?";
        try (Connection con = DatabaseConfig.getDatabaseConnection()) {
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, uuid);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                Users user = new Users();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setUuid(result.getString("uuid"));
                user.set_logged_in(result.getBoolean("is_logged_in"));
                user.set_deleted(result.getBoolean("is_deleted"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("[!] Error during get user by uuid: " + e.getMessage());
        }
        return null;
    }

    public Users findByUserEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DatabaseConfig.getDatabaseConnection()) {
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, email);
            ResultSet result = pre.executeQuery();
            if (result.next()) {
                Users user = new Users();
                user.setId(result.getInt("id"));
                user.setUsername(result.getString("username"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                user.setUuid(result.getString("uuid"));
                user.set_logged_in(result.getBoolean("is_logged_in"));
                user.set_deleted(result.getBoolean("is_deleted"));
                return user;
            }
        } catch (Exception e) {
            System.out.println("[!] Error during get user by email: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Users> findAll() {
        String sql = "SELECT * FROM users WHERE is_logged_in = false";
        try (Connection con = DatabaseConfig.getDatabaseConnection()) {
            assert con != null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<Users> list = new ArrayList<>();
            while (rs.next()) {
                Users users = new Users();
                users.setUsername(rs.getString("username"));
                users.setEmail(rs.getString("email"));
                users.setUuid(rs.getString("uuid"));
                list.add(users);
            }
            return list;
        } catch (Exception e) {
            System.out.println("Error during getting all users: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        // Not implemented
    }

    public Users deleteUser(String uuid, DeleteUserDto deleteUserDto) {
        Users user = findByUserUuid(uuid);
        if (user == null) return null;
        String sql = "UPDATE users SET is_deleted = true WHERE id = ?";
        try (Connection con = DatabaseConfig.getDatabaseConnection()) {
            assert con != null;
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setInt(1, user.getId());
            int rowAffected = pre.executeUpdate();
            return rowAffected > 0 ? user : null;
        } catch (Exception e) {
            System.out.println("Error during deleting user: " + e.getMessage());
        }
        return user;
    }

    @Override
    public Users update(Users entity) {
        return null;
    }

    public Users updateUser(String uuid, UpdateUserDto updateUserDto) {
        Users user = findByUserUuid(uuid);
        if (user != null) {
            String sql = "UPDATE users SET username = ?, email = ? WHERE uuid = ?";
            try (Connection con = DatabaseConfig.getDatabaseConnection()) {
                PreparedStatement pre = con.prepareStatement(sql);
                pre.setString(1, updateUserDto.username());
                pre.setString(2, updateUserDto.email());
                pre.setString(3, uuid);
                int rowAffected = pre.executeUpdate();
                if (rowAffected > 0) {
                    return findByUserUuid(uuid);
                }
            } catch (Exception e) {
                System.err.println("[!] Error during update user by uuid: " + e.getMessage());
            }
            return null;
        }
        throw new NoSuchElementException("Cannot find User");
    }

    public void updateIsLoggedInStatus(String uuid, boolean status) {
        String sql = "UPDATE users SET is_logged_in = ? WHERE uuid = ?";
        try (Connection con = DatabaseConfig.getDatabaseConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setBoolean(1, status);
            pre.setString(2, uuid);
            pre.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating is_logged_in: " + e.getMessage());
        }
    }
}
