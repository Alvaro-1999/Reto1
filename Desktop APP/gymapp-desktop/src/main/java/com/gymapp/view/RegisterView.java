package com.gymapp.view;

import javax.swing.*;

public class RegisterView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField txtName, txtSurname, txtEmail, txtLogin, txtBirthdate;
    public JPasswordField txtPassword, txtPasswordRepeat;
    public JButton btnRegister, btnCancel;

    public RegisterView() {
        setTitle("Registro de Usuario");
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblName = new JLabel("Nombre:");
        lblName.setBounds(50, 30, 100, 25);
        add(lblName);
        txtName = new JTextField();
        txtName.setBounds(160, 30, 150, 25);
        add(txtName);

        JLabel lblSurname = new JLabel("Apellidos:");
        lblSurname.setBounds(50, 70, 100, 25);
        add(lblSurname);
        txtSurname = new JTextField();
        txtSurname.setBounds(160, 70, 150, 25);
        add(txtSurname);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 110, 100, 25);
        add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(160, 110, 150, 25);
        add(txtEmail);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(50, 150, 100, 25);
        add(lblLogin);
        txtLogin = new JTextField();
        txtLogin.setBounds(160, 150, 150, 25);
        add(txtLogin);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 190, 100, 25);
        add(lblPassword);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 190, 150, 25);
        add(txtPassword);

        JLabel lblPasswordRepeat = new JLabel("Repetir contraseña:");
        lblPasswordRepeat.setBounds(50, 230, 120, 25);
        add(lblPasswordRepeat);
        txtPasswordRepeat = new JPasswordField();
        txtPasswordRepeat.setBounds(180, 230, 130, 25);
        add(txtPasswordRepeat);

        JLabel lblBirthdate = new JLabel("Fecha nac. (YYYY-MM-DD):");
        lblBirthdate.setBounds(50, 270, 180, 25);
        add(lblBirthdate);
        txtBirthdate = new JTextField();
        txtBirthdate.setBounds(230, 270, 100, 25);
        add(txtBirthdate);

        btnRegister = new JButton("Registrar");
        btnRegister.setBounds(80, 320, 100, 30);
        add(btnRegister);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBounds(200, 320, 100, 30);
        add(btnCancel);
    }
}
