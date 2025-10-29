package com.gymapp.controller;

import com.gymapp.model.User;
import com.gymapp.service.UserService;

public class UserController {

    private final UserService userService;

    public UserController() throws Exception {
        this.userService = new UserService();
    }

    public boolean login(String login, String password) {
        try {
            User temp = new User();
            temp.setLogin(login);
            temp.setPassword(password);
            return userService.checkCredentials(temp);
        } catch (Exception e) {
            System.err.println("Error al intentar iniciar sesión: " + e.getMessage());
            return false;
        }
    }

    public boolean register(User user) {
        try {
            if (!userService.isUserPresent(user)) {
                userService.save(user);
                return true;
            } else {
                System.out.println("El usuario ya existe.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public User getUser(String login) {
        try {
            User temp = new User();
            temp.setLogin(login);
            return userService.find(temp);
        } catch (Exception e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUser(User user) {
        try {
            userService.update(user);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(String login) {
        try {
            User temp = new User();
            temp.setLogin(login);
            userService.delete(temp);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
