package com.pawel.repository;

import com.pawel.userutils.User;

import java.util.List;

public interface IUserRepository {
    User getUser(String login);
    List<User> getUsers();
    void save();
}
