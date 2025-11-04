package com.gymapp.controller;
//o
import com.gymapp.model.Exercise;
import com.gymapp.model.Set;
import com.gymapp.model.User;
import com.gymapp.service.HistoricoService;
import com.gymapp.view.ExerciseView;
import com.gymapp.threads.timer.Cronometro;

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

    private final int pausaSerieSegundos = 5;     // Pausa previa antes de cada set (siempre aplicada)

    private Cronometro cronometroEjercicio;
    private Cronometro cronometroSet;
    private Cronometro cronometroDescanso;
    private Cronometro cronometroPausaSerie;

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
        view.lblWorkoutName.setText(
                (exercise.getWorkoutId() != null) ? "Workout ID: " + exercise.getWorkoutId().getId() : "Workout sin referencia"
        );
        view.lblExerciseName.setText("Ejercicio: " + exercise.getName());
        view.lblExerciseDesc.setText("Descripción: " + exercise.getDescription());

        List<Set> sets = exercise.getSets();
        if (sets == null) {
            sets = new ArrayList<>();
            exercise.setSets(sets);
        }

        // Ahora la tabla tiene 4 columnas: Set, Reps, Tiempo (set), Descanso (ejercicio)
        String[] cols = {"Set", "Reps", "Tiempo", "Descanso"};
        Object[][] data = new Object[sets.size()][cols.length];
        for (int i = 0; i < sets.size(); i++) {
            Set s = sets.get(i);
            data[i][0] = s.getName();
            data[i][1] = s.getReps();
            data[i][2] = s.getTime() + " seg";
            data[i][3] = exercise.getRest() + " seg"; // rest viene de exercises/{id}.rest
        }
        view.tableSets.setModel(new javax.swing.table.DefaultTableModel(data, cols));
    }

    private void handleAction() {
        String state = view.btnAction.getText();
        switch (state) {
            case "Iniciar":
                startCronometros();
                view.setButtonState("Pausar", Color.RED);
                break;
            case "Pausar":
                pauseCronometros();
                view.setButtonState("Reanudar", Color.GREEN);
                break;
            case "Reanudar":
                resumeCronometros();
                view.setButtonState("Pausar", Color.RED);
                break;
            case "Siguiente ejercicio":
                try {
                    onExerciseFinished();
                } finally {
                    view.dispose();
                }
                break;
        }
    }

    private void startCronometros() {
        // Cronómetro total del ejercicio (ascendente)
        cronometroEjercicio = new Cronometro(0, Cronometro.Tipo.ASCENDENTE, new Cronometro.CronometroListener() {
            @Override
            public void onTick(int segundos) {
                view.lblExerciseTimer.setText("Tiempo Ejercicio: " + formatTime(segundos));
            }
            @Override
            public void onFinish() {}
        });
        cronometroEjercicio.start();

        // Iniciar la secuencia: siempre pausa previa antes del primer set
        currentSetIndex = 0;
        startPausaAntesDeSet();
    }

    // Siempre se llama a startPausaAntesDeSet antes de arrancar actuallyStartSet
    private void startPausaAntesDeSet() {
        // Si no hay sets o ya se han terminado, finalizar ejercicio
        if (exercise.getSets() == null || currentSetIndex >= exercise.getSets().size()) {
            finishExercise();
            return;
        }

        // Limpiar temporizadores de UI relevantes
        view.lblRestTimer.setText("Descanso: --:--");

        // Pausa previa antes de arrancar el set actual con mensaje "Preparados..."
        cronometroPausaSerie = new Cronometro(pausaSerieSegundos, Cronometro.Tipo.DESCENDENTE, new Cronometro.CronometroListener() {
            @Override
            public void onTick(int segundos) {
                view.lblSetTimer.setText("Preparados... " + formatTime(segundos));
            }

            @Override
            public void onFinish() {
                actuallyStartSet();
            }
        });
        cronometroPausaSerie.start();
    }

    // Inicia realmente el set apuntado por currentSetIndex
    private void actuallyStartSet() {
        // Protección por si se llegó al final durante la pausa
        if (exercise.getSets() == null || currentSetIndex >= exercise.getSets().size()) {
            finishExercise();
            return;
        }

        Set set = exercise.getSets().get(currentSetIndex);
        int tiempoSet = Math.max(1, set.getTime());

        cronometroSet = new Cronometro(tiempoSet, Cronometro.Tipo.DESCENDENTE, new Cronometro.CronometroListener() {
            @Override
            public void onTick(int segundos) {
                view.lblSetTimer.setText("Set: " + formatTime(segundos));
            }

            @Override
            public void onFinish() {
                startDescanso();
            }
        });
        cronometroSet.start();
    }

    private void startDescanso() {
        int descanso = ejercicioRest();
        cronometroDescanso = new Cronometro(descanso, Cronometro.Tipo.DESCENDENTE, new Cronometro.CronometroListener() {
            @Override
            public void onTick(int segundos) {
                view.lblRestTimer.setText("Descanso: " + formatTime(segundos));
            }

            @Override
            public void onFinish() {
                
                currentSetIndex++;
                startPausaAntesDeSet();
            }
        });
        cronometroDescanso.start();
    }

    private int ejercicioRest() {
        return exercise.getRest();
    }

    private void pauseCronometros() {
        if (cronometroEjercicio != null) cronometroEjercicio.pause();
        if (cronometroSet != null) cronometroSet.pause();
        if (cronometroDescanso != null) cronometroDescanso.pause();
        if (cronometroPausaSerie != null) cronometroPausaSerie.pause();
    }

    private void resumeCronometros() {
        if (cronometroEjercicio != null) cronometroEjercicio.resume();
        if (cronometroSet != null) cronometroSet.resume();
        if (cronometroDescanso != null) cronometroDescanso.resume();
        if (cronometroPausaSerie != null) cronometroPausaSerie.resume();
    }

    private void finishExercise() {
        if (cronometroEjercicio != null) cronometroEjercicio.stop();
        if (cronometroSet != null) cronometroSet.stop();
        if (cronometroDescanso != null) cronometroDescanso.stop();
        if (cronometroPausaSerie != null) cronometroPausaSerie.stop();

        view.setButtonState("Siguiente ejercicio", Color.BLUE);

        try {
            if (historicoId != null && historicoService != null) {
                int tiempoTotal = (cronometroEjercicio != null) ? cronometroEjercicio.getSegundos() : 0;
                historicoService.updateCompletion(historicoId, 100, tiempoTotal);
            }
        } catch (Exception ex) {
            System.err.println("Error actualizando histórico: " + ex.getMessage());
        }

        int tiempoTotal = (cronometroEjercicio != null) ? cronometroEjercicio.getSegundos() : 0;
        JOptionPane.showMessageDialog(view,
                "Ejercicio completado: " + exercise.getName() +
                        "\nTiempo total: " + formatTime(tiempoTotal),
                "Completado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitExercise() {
        if (cronometroEjercicio != null) cronometroEjercicio.stop();
        if (cronometroSet != null) cronometroSet.stop();
        if (cronometroDescanso != null) cronometroDescanso.stop();
        if (cronometroPausaSerie != null) cronometroPausaSerie.stop();

        int tiempoTotal = (cronometroEjercicio != null) ? cronometroEjercicio.getSegundos() : 0;
        JOptionPane.showMessageDialog(view,
                "Resumen del workout:\n" +
                        "Ejercicio: " + exercise.getName() +
                        "\nTiempo total: " + formatTime(tiempoTotal),
                "Resumen", JOptionPane.INFORMATION_MESSAGE);
        view.dispose();
    }

    public void onExerciseFinished() {
    }

    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
