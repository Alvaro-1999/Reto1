package com.gymapp.view;

import com.gymapp.model.Workout;

import javax.swing.*;
import java.util.List;

public class WorkoutView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JList<String> listWorkouts;
    public JTable tableExercises;
    public JButton btnShowSets;

    private List<Workout> workouts;

    public WorkoutView() {
        setTitle("Workouts - RetoGym");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblWorkouts = new JLabel("Workouts disponibles:");
        lblWorkouts.setBounds(30, 20, 200, 25);
        add(lblWorkouts);

        listWorkouts = new JList<>();
        JScrollPane scrollWorkouts = new JScrollPane(listWorkouts);
        scrollWorkouts.setBounds(30, 50, 250, 200);
        add(scrollWorkouts);

        btnShowSets = new JButton("Ver ejercicios");
        btnShowSets.setBounds(30, 270, 150, 30);
        add(btnShowSets);

        JLabel lblExercises = new JLabel("Ejercicios del workout:");
        lblExercises.setBounds(320, 20, 200, 25);
        add(lblExercises);

        tableExercises = new JTable();
        JScrollPane scrollExercises = new JScrollPane(tableExercises);
        scrollExercises.setBounds(320, 50, 340, 250);
        add(scrollExercises);

        setVisible(true);
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void updateExerciseTable(List<String[]> rows) {
        String[] columns = {"Nombre", "Descripción", "Descanso"};
        String[][] data = rows.toArray(new String[0][]);
        tableExercises.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }
}
