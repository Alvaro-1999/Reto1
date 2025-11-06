package com.gymapp.view;

import javax.swing.*;
import java.awt.*;

public class ExerciseView extends JFrame {
    private static final long serialVersionUID = 1L;

    public JLabel lblWorkoutName;

    public JLabel lblExerciseName;
    public JLabel lblExerciseDesc;

    public JLabel lblExerciseTimer;
    public JLabel lblSetTimer;
    public JLabel lblRestTimer;

    public JTable tableSets;

    public JButton btnAction;
    public JButton btnExit;

    public ExerciseView() {
        setTitle("Ejercicio en curso");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        lblWorkoutName = new JLabel("Workout: ---", SwingConstants.CENTER);
        lblExerciseName = new JLabel("Ejercicio: ---", SwingConstants.CENTER);
        lblExerciseDesc = new JLabel("Descripción: ---", SwingConstants.CENTER);

        topPanel.add(lblWorkoutName);
        topPanel.add(lblExerciseName);
        topPanel.add(lblExerciseDesc);

        JPanel sidePanel = new JPanel(new GridLayout(3, 1, 5, 5));
        lblExerciseTimer = new JLabel("Tiempo Ejercicio: 00:00", SwingConstants.CENTER);
        lblSetTimer = new JLabel("Set: 00:00", SwingConstants.CENTER);
        lblRestTimer = new JLabel("Descanso: --:--", SwingConstants.CENTER);

        sidePanel.add(lblExerciseTimer);
        sidePanel.add(lblSetTimer);
        sidePanel.add(lblRestTimer);

        String[] cols = {"Set", "Reps", "Tiempo", "Descanso"};
        tableSets = new JTable(new Object[0][cols.length], cols);
        JScrollPane scrollSets = new JScrollPane(tableSets);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnAction = new JButton("Iniciar");
        btnAction.setBackground(Color.GREEN);
        btnAction.setForeground(Color.WHITE);
        btnExit = new JButton("Salir");
        btnExit.setBackground(Color.RED);
        btnExit.setForeground(Color.WHITE);
        bottomPanel.add(btnAction);
        bottomPanel.add(btnExit);

        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(scrollSets, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setButtonState(String text, Color color) {
        btnAction.setText(text);
        btnAction.setBackground(color);
        btnAction.setForeground(Color.WHITE);
    }
}
