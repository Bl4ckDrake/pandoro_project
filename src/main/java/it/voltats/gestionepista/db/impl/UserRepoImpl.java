package it.voltats.gestionepista.db.impl;

import it.voltats.gestionepista.db.DbHelper;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.Gender;
import it.voltats.gestionepista.db.repo.UserRepo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserRepoImpl implements UserRepo {
    private final Connection connection = DbHelper.getConnection();

    @Override
    public void insert(User user) {
        final String QUERY = "INSERT INTO user(name, surname, email, phone_number, cf, birthdate, gender) VALUES(?,?,?,?,?,?,?)";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getCf());
            statement.setString(6, new SimpleDateFormat("dd-MM-yyyy").format(user.getBirthdate()));
            statement.setString(7, user.getGender().name());

            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        final String QUERY = "UPDATE user SET (name, surname, email, phone_number, cf, birthdate, gender) VALUES(?,?,?,?,?,?,?) WHERE id=?";


    }

    @Override
    public void delete(User user) {
        final String QUERY = "DELETE * FROM user WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);

            statement.setString(1, String.valueOf(user.getId()));

            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(int userId) {
        final String QUERY = "SELECT * FROM user WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);

            statement.setString(1, String.valueOf(userId));

            ResultSet resultSet = statement.executeQuery();

            User result = new User(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    Gender.valueOf(resultSet.getString("gender")),
                    new SimpleDateFormat("dd-MM-yyyy").parse(resultSet.getString("birthdate")),
                    resultSet.getString("cf"),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number")
                );
            result.setId(resultSet.getInt("id"));

            return result;

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
