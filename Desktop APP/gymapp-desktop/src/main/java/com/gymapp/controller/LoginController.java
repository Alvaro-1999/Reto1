package com.gymapp.controller;

import com.gymapp.backups.BackupService;
import com.gymapp.model.User;
import com.gymapp.service.BackupManager;
import com.gymapp.service.FirebaseConfig;
import com.gymapp.service.UserService;
import com.gymapp.util.ConnectionGestor;
import com.gymapp.view.LoginView;
import com.gymapp.view.MainView;
import com.gymapp.view.RegisterView;

import javax.swing.*;

public class LoginController {

    private final UserService userService;
    private final LoginView view;
    private final BackupService backupService;
    private final BackupManager backupManager;

    public LoginController(UserService userService, LoginView view) {
        this.userService = userService;
        this.view = view;
        this.backupService = new BackupService();
        this.backupManager = new BackupManager();

        this.view.btnLogin.addActionListener(e -> login());
        this.view.btnRegister.addActionListener(e -> register());
    }

    private void login() {
        try {
            String login = view.txtLogin.getText().trim();
            String password = new String(view.txtPassword.getPassword()).trim();

            if (login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Debes introducir login y contraseña", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User loggedUser;

            if (ConnectionGestor.hayConexion()) {
                FirebaseConfig.initFirebase();
                backupService.performFullBackup();

                User user = new User();
                user.setLogin(login);
                user.setPassword(password);

                if (!userService.checkCredentials(user)) {
                    JOptionPane.showMessageDialog(view,
                            "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                loggedUser = userService.find(user);
                JOptionPane.showMessageDialog(view,
                        "Bienvenido " + loggedUser.getName() + " (nivel " + loggedUser.getLevel() + ")");

            } else {
                backupManager.cargarBackupOffline();
                User offlineUser = backupManager.buscarUsuarioOffline(login, password);
                if (offlineUser == null) {
                    JOptionPane.showMessageDialog(view,
                            "Usuario o contraseña incorrectos o no disponibles offline",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                loggedUser = offlineUser;
                JOptionPane.showMessageDialog(view,
                        "Modo offline: Bienvenido " + loggedUser.getName() +
                                " (nivel " + loggedUser.getLevel() + ")");
            }

            abrirMainView(loggedUser);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Error en login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirMainView(User user) {
        view.dispose();
        MainView mainView = new MainView(user.getName());
        new MainController(mainView, user);
        mainView.setVisible(true);
    }

    private void register() {
        RegisterView registerView = new RegisterView();
        new RegisterController(userService, registerView);
        registerView.setVisible(true);
    }
}
