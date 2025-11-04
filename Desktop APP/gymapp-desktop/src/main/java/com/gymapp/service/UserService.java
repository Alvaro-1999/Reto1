package com.gymapp.service;

import com.gymapp.model.User;
import java.io.IOException;
import java.util.List;

public class UserService implements ServiceInterface<User> {

    private final UserResource userResource;

    public UserService() throws IOException {
        this.userResource = new UserResource();
    }

    @Override
    public void save(User user) throws Exception {
        if (!isUserPresent(user)) {
            userResource.save(user);
        }
    }

    @Override
    public User find(User user) throws Exception {
        return userResource.find(user);
    }

    @Override
    public List<User> findAll() throws Exception {
        return userResource.findAll();
    }

    @Override
    public void update(User user) throws Exception {
        userResource.update(user);
    }

    @Override
    public void delete(User user) throws Exception {
        userResource.delete(user);
    }

    public boolean isUserPresent(User user) throws Exception {
        User found = find(user);
        return found != null && found.getLogin() != null;
    }

    public boolean checkCredentials(User user) throws Exception {
        User existingUser = find(user);
        return existingUser != null
            && existingUser.getLogin().equalsIgnoreCase(user.getLogin())
            && existingUser.getPassword().equals(user.getPassword());
    }

    public User createUser(String name, String lastName, String login, String mail, String password, String birthDate) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setLastName(lastName);
        newUser.setLogin(login);
        newUser.setMail(mail);
        newUser.setPassword(password);
        newUser.setBirthDate(birthDate);
        newUser.setLevel(0); 
        return newUser;
    }
}
