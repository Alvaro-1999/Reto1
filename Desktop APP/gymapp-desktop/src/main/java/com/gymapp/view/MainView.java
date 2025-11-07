package com.gymapp.view;

import javax.swing.*;

public class MainView extends JFrame {
    public JButton btnWorkouts;
    public JButton btnHistory;
    public JButton btnExit;
    public JLabel lblWelcome; 

    public MainView(String userName) {
        setTitle("Menú Principal - RetoGym");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null); 

        lblWelcome = new JLabel("Bienvenido, " + userName);
        lblWelcome.setBounds(100, 30, 250, 25);
        add(lblWelcome);

        btnWorkouts = new JButton("Ver Workouts");
        btnWorkouts.setBounds(120, 80, 150, 40);
        add(btnWorkouts);

        btnHistory = new JButton("Ver Histórico");
        btnHistory.setBounds(120, 140, 150, 40);
        add(btnHistory);

        btnExit = new JButton("Salir");
        btnExit.setBounds(120, 200, 150, 40);
        add(btnExit);

        setVisible(true);
    }
}
