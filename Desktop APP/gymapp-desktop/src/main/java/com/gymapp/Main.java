package com.gymapp;

import com.gymapp.service.BackupManager;
import com.gymapp.service.FirebaseConfig;
import com.gymapp.service.UserService;
 import com.gymapp.view.LoginView;

import java.util.List;

import com.gymapp.backups.BackupService;
import com.gymapp.controller.LoginController;
import com.gymapp.model.Exercise;
import com.gymapp.model.Historico;
import com.gymapp.model.User;
import com.gymapp.model.Workout;

public class Main {
    public static void main(String[] args) throws Exception {
     
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
