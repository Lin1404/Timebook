package com.timebook.timebook.models.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public List<User> findByEmailContains(String string);
}
