package com.gymapp;

import com.gymapp.service.FirebaseConfig;
import com.gymapp.service.UserService;
import com.gymapp.view.LoginView;
import com.gymapp.controller.LoginController;

public class Main {
    public static void main(String[] args) {
        try {
            // Inicializar Firebase
            FirebaseConfig.initFirebase();

            // Crear servicios y vistas
            UserService userService = new UserService();
            LoginView loginView = new LoginView();

            // Conectar controlador
            new LoginController(userService, loginView);

            // Mostrar ventana de login
            loginView.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error iniciando la aplicación: " + e.getMessage());
        }
    }
}
