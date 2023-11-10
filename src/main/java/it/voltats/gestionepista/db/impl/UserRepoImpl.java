package it.voltats.gestionepista.db.impl;

import it.voltats.gestionepista.db.DbHelper;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.Gender;
import it.voltats.gestionepista.db.repo.UserRepo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserRepoImpl implements UserRepo {
    private final Connection connection = DbHelper.getConnection();

    @Override
    public void insert(User user) {
        final String QUERY = "INSERT INTO user(name, surname, gender, birthdate, cf, email, phone_number) VALUES(?,?,?,?,?,?,?)";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getGender().name());
            statement.setString(4, new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()));
            statement.setString(5, user.getCf());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getPhoneNumber());

            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        final String QUERY = "UPDATE user SET (name, surname, gender, birthdate, cf, email, phone_number) = (?,?,?,?,?,?,?) WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getGender().name());
            statement.setString(4, new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()));
            statement.setString(5, user.getCf());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getPhoneNumber());
            statement.setInt(8, user.getId());

            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
    public List<User> findAll() {
        final String QUERY = "SELECT * FROM user";

        try {
            var statement = connection.prepareStatement(QUERY);

            ResultSet resultSet = statement.executeQuery();

            List<User> result = new ArrayList<>();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        Gender.valueOf(resultSet.getString("gender")),
                        new SimpleDateFormat("dd/MM/yyyy").parse(resultSet.getString("birthdate")),
                        resultSet.getString("cf"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"));

                        user.setId(resultSet.getInt("id"));


                result.add(user);
            }

            return result;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User findById(int userId) {
        final String QUERY = "SELECT * FROM user WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);

            statement.setString(1, String.valueOf(userId));

            ResultSet resultSet = statement.executeQuery();

            /*String s = resultSet.getString("gender");
            String s1 = resultSet.getString("birthdate");
            Gender g;
            Date d;
            if(s == null)
                g = null;
            else
                g = Gender.valueOf(s);

            if(s1 == null)
                d = null;
            else
                d = new SimpleDateFormat("dd/MM/yyyy").parse(s1);
            */
            User result = new User(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    Gender.valueOf(resultSet.getString("gender")),
                    new SimpleDateFormat("dd/MM/yyyy").parse(resultSet.getString("birthdate")),
                    resultSet.getString("cf"),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number"));
            result.setId(resultSet.getInt("id"));

            return result;

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        final String QUERY = "SELECT * FROM user WHERE email=?";

        try {
            var statement = connection.prepareStatement(QUERY);

            statement.setString(1, String.valueOf(email));

            ResultSet resultSet = statement.executeQuery();

            User result = new User(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    Gender.valueOf(resultSet.getString("gender").toUpperCase().trim()),
                    new SimpleDateFormat("dd/MM/yyyy").parse(resultSet.getString("birthdate")),
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
