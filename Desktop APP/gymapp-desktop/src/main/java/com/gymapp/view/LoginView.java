package com.gymapp.view;

import javax.swing.*;

public class LoginView extends JFrame {
    public JTextField txtLogin;
    public JPasswordField txtPassword;
    public JButton btnLogin;
    public JButton btnRegister;

    public LoginView() {
        setTitle("Login - RetoGym");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setBounds(50, 40, 100, 25);
        add(lblUser);

        txtLogin = new JTextField();
        txtLogin.setBounds(150, 40, 180, 25);
        add(txtLogin);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(50, 80, 100, 25);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 80, 180, 25);
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 140, 120, 30);
        add(btnLogin);

        btnRegister = new JButton("Registrar");
        btnRegister.setBounds(210, 140, 120, 30);
        add(btnRegister);

        setVisible(true);
    }
}
