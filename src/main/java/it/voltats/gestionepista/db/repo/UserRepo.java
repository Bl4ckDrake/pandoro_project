package it.voltats.gestionepista.db.repo;

import it.voltats.gestionepista.db.entity.User;

public interface UserRepo {
    public void insert(User user);
    public void update(User user);
    public void delete(User user);
}
