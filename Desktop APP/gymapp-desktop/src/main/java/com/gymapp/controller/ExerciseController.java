package com.gymapp.controller;

import com.gymapp.model.Exercise;
import com.gymapp.model.Set;
import com.gymapp.model.User;
import com.gymapp.service.HistoricoService;
import com.gymapp.view.ExerciseView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseController {
    private final ExerciseView view;
    private final Exercise exercise;
    private final String historicoId;
    private final HistoricoService historicoService;
    private final User loggedUser;

    private int currentSetIndex = 0;
    private Timer exerciseTimer;
    private Timer setTimer;
    private int exerciseSeconds = 0;
    private int setSecondsRemaining;

    public ExerciseController(ExerciseView view, Exercise exercise,
                              String historicoId, HistoricoService historicoService, User loggedUser) {
        this.view = view;
        this.exercise = exercise;
        this.historicoId = historicoId;
        this.historicoService = historicoService;
        this.loggedUser = loggedUser;

        setupExercise();

        view.btnAction.addActionListener(e -> handleAction());
        view.btnExit.addActionListener(e -> exitExercise());
    }

    private void setupExercise() {
        if (exercise.getWorkoutId() != null) {
            view.lblWorkoutName.setText("Workout ID: " + exercise.getWorkoutId().getId());
        } else {
            view.lblWorkoutName.setText("Workout sin referencia");
        }

        view.lblExerciseName.setText("Ejercicio: " + exercise.getName());
        view.lblExerciseDesc.setText("Descripción: " + exercise.getDescription());

        List<Set> sets = exercise.getSets();
        if (sets == null) {
            sets = new ArrayList<>();
            exercise.setSets(sets);
        }

        Object[][] data = new Object[sets.size()][3];
        for (int i = 0; i < sets.size(); i++) {
            Set s = sets.get(i);
            data[i][0] = s.getName();
            data[i][1] = s.getReps();
            data[i][2] = s.getTime() + " seg";
        }
        String[] cols = {"Set", "Reps", "Tiempo"};
        view.tableSets.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }

    private void handleAction() {
        String state = view.btnAction.getText();
        switch (state) {
            case "Iniciar":
                startExerciseTimer();
                if (!exercise.getSets().isEmpty()) {
                    startSet();
                }
                view.setButtonState("Pausar", Color.RED);
                break;
            case "Pausar":
                pauseTimers();
                view.setButtonState("Reanudar", Color.GREEN);
                break;
            case "Reanudar":
                resumeTimers();
                view.setButtonState("Pausar", Color.RED);
                break;
            case "Siguiente ejercicio":
                finishExercise();
                break;
        }
    }

    private void startExerciseTimer() {
        exerciseTimer = new Timer(1000, e -> {
            exerciseSeconds++;
            view.lblExerciseTimer.setText("Tiempo Ejercicio: " + formatTime(exerciseSeconds));
        });
        exerciseTimer.start();
    }

    private void startSet() {
        if (currentSetIndex >= exercise.getSets().size()) {
            finishExercise();
            return;
        }

        Set set = exercise.getSets().get(currentSetIndex);
        setSecondsRemaining = set.getTime();

        setTimer = new Timer(1000, e -> {
            setSecondsRemaining--;
            view.lblRestTimer.setText("Set: " + formatTime(setSecondsRemaining));
            if (setSecondsRemaining <= 0) {
                setTimer.stop();
                currentSetIndex++;
                if (currentSetIndex < exercise.getSets().size()) {
                    startSet();
                } else {
                    finishExercise();
                }
            }
        });
        setTimer.start();
    }

    private void pauseTimers() {
        if (exerciseTimer != null) exerciseTimer.stop();
        if (setTimer != null) setTimer.stop();
    }

    private void resumeTimers() {
        if (exerciseTimer != null) exerciseTimer.start();
        if (setTimer != null) setTimer.start();
    }

    private void finishExercise() {
        pauseTimers();
        view.setButtonState("Siguiente ejercicio", Color.BLUE);

        try {
            if (historicoId != null && historicoService != null) {
                historicoService.updateCompletion(historicoId, 100, exerciseSeconds);
            }
        } catch (Exception ex) {
            System.err.println("Error actualizando histórico: " + ex.getMessage());
        }

        JOptionPane.showMessageDialog(view,
                "Ejercicio completado: " + exercise.getName() +
                        "\nTiempo total: " + formatTime(exerciseSeconds),
                "Completado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitExercise() {
        pauseTimers();
        JOptionPane.showMessageDialog(view,
                "Resumen del workout:\n" +
                        "Ejercicio: " + exercise.getName() +
                        "\nTiempo total: " + formatTime(exerciseSeconds),
                "Resumen", JOptionPane.INFORMATION_MESSAGE);
        view.dispose();
    }

    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
