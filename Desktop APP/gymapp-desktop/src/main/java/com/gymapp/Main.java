package com.gymapp;

import com.gymapp.service.FirebaseConfig;
import com.gymapp.service.UserService;
import com.gymapp.view.LoginView;
import com.gymapp.controller.LoginController;

public class Main {
    public static void main(String[] args) {
        try {
            FirebaseConfig.initFirebase();

            UserService userService = new UserService();
            LoginView loginView = new LoginView();

            new LoginController(userService, loginView);

            loginView.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error iniciando la aplicación: " + e.getMessage());
        }
    }
}
