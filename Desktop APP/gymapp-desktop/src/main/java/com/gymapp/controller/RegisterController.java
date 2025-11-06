package com.gymapp.controller;

import com.gymapp.model.User;
import com.gymapp.service.UserService;
import com.gymapp.view.RegisterView;

import javax.swing.*;

public class RegisterController {
    private final UserService userService;
    private final RegisterView view;

    public RegisterController(UserService userService, RegisterView view) {
        this.userService = userService;
        this.view = view;

        view.btnRegister.addActionListener(e -> register());
        view.btnCancel.addActionListener(e -> view.dispose());
    }

    private void register() {
        try {
            String name = view.txtName.getText().trim();
            String surname = view.txtSurname.getText().trim();
            String email = view.txtEmail.getText().trim();
            String login = view.txtLogin.getText().trim();
            String password = new String(view.txtPassword.getPassword()).trim();
            String passwordRepeat = new String(view.txtPasswordRepeat.getPassword()).trim();
            String birthdate = view.txtBirthdate.getText().trim();

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                login.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios");
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(view, "Email no válido");
                return;
            }

            if (!password.equals(passwordRepeat)) {
                JOptionPane.showMessageDialog(view, "Las contraseñas no coinciden");
                return;
            }

            User temp = new User();
            temp.setLogin(login);
            if (userService.isUserPresent(temp)) {
                JOptionPane.showMessageDialog(view, "El login ya existe, elige otro");
                return;
            }

            User newUser = userService.createUser(name, surname, login, email, password, birthdate);
            userService.save(newUser);

            JOptionPane.showMessageDialog(view, "Usuario registrado correctamente");
            view.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error en registro: " + ex.getMessage());
        }
    }
}
