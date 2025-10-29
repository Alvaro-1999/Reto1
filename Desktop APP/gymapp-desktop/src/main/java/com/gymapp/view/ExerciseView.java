package com.gymapp.view;

import javax.swing.*;
import java.awt.*;

public class ExerciseView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Labels principales
    public JLabel lblWorkoutTimer;
    public JLabel lblWorkoutName;
    public JLabel lblExerciseName;
    public JLabel lblExerciseDesc;
    public JLabel lblExerciseTimer;
    public JLabel lblRestTimer;

    // Tabla de sets
    public JTable tableSets;

    // Botones
    public JButton btnAction;
    public JButton btnExit;

    public ExerciseView() {
        setTitle("Ejercicio en curso");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel superior (cronómetro global y datos del ejercicio)
        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        lblWorkoutTimer = new JLabel("Cronómetro Workout: 00:00", SwingConstants.CENTER);
        lblWorkoutName = new JLabel("Workout: ---", SwingConstants.CENTER);
        lblExerciseName = new JLabel("Ejercicio: ---", SwingConstants.CENTER);
        lblExerciseDesc = new JLabel("Descripción: ---", SwingConstants.CENTER);

        topPanel.add(lblWorkoutTimer);
        topPanel.add(lblWorkoutName);
        topPanel.add(lblExerciseName);
        topPanel.add(lblExerciseDesc);

        // Panel lateral (cronómetros de ejercicio y descanso)
        JPanel sidePanel = new JPanel(new GridLayout(2, 1));
        lblExerciseTimer = new JLabel("Tiempo Ejercicio: 00:00", SwingConstants.CENTER);
        lblRestTimer = new JLabel("Descanso: --:--", SwingConstants.CENTER);
        sidePanel.add(lblExerciseTimer);
        sidePanel.add(lblRestTimer);

        // Tabla de sets
        String[] cols = {"Set", "Reps", "Tiempo"};
        tableSets = new JTable(new Object[0][cols.length], cols);
        JScrollPane scrollSets = new JScrollPane(tableSets);

        // Panel inferior (botones)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnAction = new JButton("Iniciar");
        btnAction.setBackground(Color.GREEN);
        btnExit = new JButton("Salir");
        btnExit.setBackground(Color.RED);
        bottomPanel.add(btnAction);
        bottomPanel.add(btnExit);

        // Layout principal
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(scrollSets, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Método auxiliar para cambiar estado del botón principal
    public void setButtonState(String text, Color color) {
        btnAction.setText(text);
        btnAction.setBackground(color);
    }
}
