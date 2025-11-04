package com.gymapp.controller;
//o
import com.gymapp.model.User;
import com.gymapp.model.Workout;
import com.gymapp.model.Exercise;
import com.gymapp.model.Set;
import com.gymapp.service.WorkoutService;
import com.gymapp.service.HistoricoService;
import com.gymapp.service.SetService;
import com.gymapp.view.WorkoutView;
import com.gymapp.view.ExerciseView;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutController {

    private final WorkoutService workoutService;
    private final HistoricoService historicoService;
    private final SetService setService;
    private final WorkoutView view;
    private final User loggedUser;
    private final Firestore db;
    private final HistoricoController historicoController;

    private List<Exercise> currentExercises;
    private int currentExerciseIndex = 0;
    private Workout selectedWorkout;
    private long workoutStartMillis;
    private String historicoId;

    public WorkoutController(WorkoutService workoutService, WorkoutView view, User loggedUser, HistoricoController historicoController) {
        this.workoutService = workoutService;
        this.historicoService = new HistoricoService();
        this.view = view;
        this.loggedUser = loggedUser;
        this.db = workoutService.getDB();
        this.setService = new SetService(db);
        this.historicoController = historicoController;

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
        this.view.listWorkouts.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = view.listWorkouts.getSelectedIndex();
                if (index >= 0) {
                    selectedWorkout = view.getWorkouts().get(index);
                    try {
                        List<Exercise> exercises = workoutService.getExercisesFromWorkout(selectedWorkout);
                        for (Exercise ex : exercises) {
                            List<Set> sets = setService.findByExercise(db.collection("exercises").document(ex.getId()));
                            ex.setSets(sets);
                        }
                        currentExercises = exercises;

                        List<String[]> rows = exercises.stream()
                                .map(ex -> new String[]{ex.getName(), ex.getDescription(), ex.getRest() + " seg"})
                                .collect(Collectors.toList());
                        view.updateExerciseTable(rows);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view,
                                "Error cargando ejercicios: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        this.view.btnShowSets.addActionListener(e -> startWorkoutExecution());
    }

    private void startWorkoutExecution() {
        if (selectedWorkout == null || currentExercises == null || currentExercises.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Selecciona primero un workout con ejercicios.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            currentExerciseIndex = 0;
            workoutStartMillis = System.currentTimeMillis();

            String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            DocumentReference workoutRef = db.collection("workouts").document(selectedWorkout.getId());

            int tiempoEstimado = calcularTiempoEstimado(currentExercises);

            DocumentReference historicoRef = historicoService.save(
                    loggedUser,
                    selectedWorkout.getWorkoutName(),
                    today,
                    tiempoEstimado,
                    0,
                    0,
                    selectedWorkout.getLevel(),
                    workoutRef
            );

            historicoId = historicoRef.getId();

            launchExercise(currentExercises.get(currentExerciseIndex));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error iniciando workout: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void launchExercise(Exercise exercise) {
        try {
            // 🔑 Recargar sets del ejercicio antes de abrir la vista
            List<Set> sets = setService.findByExercise(db.collection("exercises").document(exercise.getId()));
            exercise.setSets(sets);

            ExerciseView exerciseView = new ExerciseView();

            ExerciseController ec = new ExerciseController(
                    exerciseView,
                    exercise,
                    historicoId,
                    historicoService,
                    loggedUser
            ) {
                @Override
                public void onExerciseFinished() {
                    currentExerciseIndex++;

                    int ejerciciosRealizados = currentExerciseIndex;
                    int totalEjercicios = currentExercises.size();
                    int progreso = (int) (((double) ejerciciosRealizados / totalEjercicios) * 100);
                    int tiempoTranscurrido = (int) ((System.currentTimeMillis() - workoutStartMillis) / 1000);

                    try {
                        historicoService.updateCompletion(historicoId, progreso, tiempoTranscurrido);
                    } catch (Exception ex) {
                        System.err.println("Error actualizando progreso: " + ex.getMessage());
                    }

                    if (currentExerciseIndex < totalEjercicios) {
                        SwingUtilities.invokeLater(() -> launchExercise(currentExercises.get(currentExerciseIndex)));
                    } else {
                        JOptionPane.showMessageDialog(view,
                                "¡Workout completado!\n" +
                                        "Tiempo total: " + formatTime(tiempoTranscurrido) + "\n" +
                                        "Ejercicios completados: " + ejerciciosRealizados + " de " + totalEjercicios + " (" + progreso + "%)\n" +
                                        "¡Gran trabajo, sigue así!",
                                "Resumen Workout", JOptionPane.INFORMATION_MESSAGE);

                        if (selectedWorkout.getLevel() == loggedUser.getLevel()) {
                            int nuevoNivel = loggedUser.getLevel() + 1;
                            loggedUser.setLevel(nuevoNivel);
                            try {
                                db.collection("users").document(loggedUser.getId())
                                        .update("level", nuevoNivel);
                                JOptionPane.showMessageDialog(view,
                                        "¡Felicidades! Has desbloqueado el nivel " + nuevoNivel,
                                        "Nivel desbloqueado", JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                System.err.println("Error actualizando nivel de usuario: " + ex.getMessage());
                            }
                            loadWorkouts();
                        }

                        if (historicoController != null) {
                            historicoController.loadHistorico();
                        }
                    }
                }
            };

            exerciseView.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error lanzando ejercicio: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int calcularTiempoEstimado(List<Exercise> ejercicios) {
        final int pausaAntesDeSet = 5;
        int totalSegundos = 0;

        for (Exercise ejercicio : ejercicios) {
            List<Set> sets = ejercicio.getSets();
            if (sets == null || sets.isEmpty()) continue;

            for (Set set : sets) {
                totalSegundos += pausaAntesDeSet;       // preparación antes del set
                totalSegundos += set.getTime();         // duración del set
                totalSegundos += ejercicio.getRest();   // descanso después del set
            }
        }

        return totalSegundos;
    }

    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
