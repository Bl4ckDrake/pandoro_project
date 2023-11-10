package it.voltats.gestionepista.db.repo;

import it.voltats.gestionepista.db.entity.User;

import java.util.List;

public interface UserRepo {
    public void insert(User user);
    public void update(User user);
    public void delete(User user);
    public User findById(int userId);
    public User findByEmail(String email);

    public List<User> findAll();
}
