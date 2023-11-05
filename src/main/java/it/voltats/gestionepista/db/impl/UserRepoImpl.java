package it.voltats.gestionepista.db.impl;

import it.voltats.gestionepista.db.DbHelper;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.repo.UserRepo;

import java.sql.Connection;

public class UserRepoImpl implements UserRepo {
    private final Connection connection = DbHelper.getConnection();

    @Override
    public void insert(User user) {
        final String QUERY = "INSERT INTO user(name, surname, email, phone_number, cf) VALUES(?,?,?,?,?)";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getCf());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
