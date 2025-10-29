package com.gymapp.controller;

import com.gymapp.model.User;
import com.gymapp.model.Workout;
import com.gymapp.model.Set;
import com.gymapp.model.Exercise;
import com.gymapp.service.WorkoutService;
import com.gymapp.service.HistoricoService;
import com.gymapp.view.WorkoutView;
import com.gymapp.view.ExerciseView;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkoutController {

    private final WorkoutService workoutService;
    private final HistoricoService historicoService;
    private final WorkoutView view;
    private final User loggedUser;
    private final Firestore db;

    public WorkoutController(WorkoutService workoutService, WorkoutView view, User loggedUser) {
        this.workoutService = workoutService;
        this.historicoService = new HistoricoService();
        this.view = view;
        this.loggedUser = loggedUser;
        this.db = workoutService.getDB();

        loadWorkouts();
        setupListeners();
    }

    private void loadWorkouts() {
        try {
            List<Workout> workouts = workoutService.findByLevelOrBelow(loggedUser.getLevel());
            DefaultListModel<String> model = new DefaultListModel<>();
            for (Workout w : workouts) {
                model.addElement(w.getWorkoutName() + " (nivel " + w.getLevel() + ")");
            }
            view.listWorkouts.setModel(model);
            view.setWorkouts(workouts);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando workouts: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupListeners() {
        // Mostrar ejercicios al seleccionar un workout
        this.view.listWorkouts.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = view.listWorkouts.getSelectedIndex();
                if (index >= 0) {
                    Workout selected = view.getWorkouts().get(index);
                    try {
                        List<Exercise> exercises = workoutService.getExercisesFromWorkout(selected);

                        List<String[]> rows = new ArrayList<>();
                        for (Exercise ex : exercises) {
                            rows.add(new String[]{
                                ex.getName(),
                                ex.getDescription(),
                                ex.getRest() + " seg"
                            });
                        }

                        view.updateExerciseTable(rows);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view,
                                "Error cargando ejercicios: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Abrir pantalla de ejecución
        this.view.btnShowSets.addActionListener(e -> openExerciseScreen());
    }

    private void openExerciseScreen() {
        int index = view.listWorkouts.getSelectedIndex();
        if (index >= 0) {
            Workout selected = view.getWorkouts().get(index);
            try {
                // Cargar todos los sets de todos los ejercicios del workout
                List<Set> sets = workoutService.getSetsFromWorkout(selected);

                // Crear histórico
                String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                DocumentReference workoutRef = db.collection("workouts").document(selected.getId());

                DocumentReference historicoRef = historicoService.save(
                        loggedUser,
                        selected.getWorkoutName(),
                        today,
                        selected.getTiempo(),
                        0,
                        0,
                        selected.getLevel(),
                        workoutRef
                );

                // Crear Exercise genérico para ejecutar todos los sets
                Exercise exercise = new Exercise();
                exercise.setName(selected.getWorkoutName());
                exercise.setDescription("Ejercicios del workout " + selected.getWorkoutName());
                exercise.setRest(10); // puedes ajustar el descanso general
                exercise.setSets(sets != null ? sets : new ArrayList<>());
                exercise.setWorkoutId(workoutRef);

                // Abrir vista de ejecución
                ExerciseView exerciseView = new ExerciseView();
                new ExerciseController(exerciseView, exercise, historicoRef.getId(), historicoService, loggedUser);
                exerciseView.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Error cargando sets: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}