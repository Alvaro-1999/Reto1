package com.gymapp.controller;

import com.gymapp.model.User;
import com.gymapp.service.WorkoutService;
import com.gymapp.service.HistoricoService;
import com.gymapp.view.MainView;
import com.gymapp.view.WorkoutView;
import com.gymapp.view.HistoricoView;

import javax.swing.*;

public class MainController {
    private final MainView view;
    private final User loggedUser;

    public MainController(MainView view, User loggedUser) {
        this.view = view;
        this.loggedUser = loggedUser;

        // Mostrar saludo en la vista principal
        this.view.lblWelcome.setText("Bienvenido, " + loggedUser.getName() + " (Nivel " + loggedUser.getLevel() + ")");

        // Eventos
        this.view.btnWorkouts.addActionListener(e -> openWorkouts());
        this.view.btnHistory.addActionListener(e -> openHistorico());
        this.view.btnExit.addActionListener(e -> exitApp());
    }

    private void openWorkouts() {
        try {
            WorkoutService workoutService = new WorkoutService();
            WorkoutView workoutView = new WorkoutView();
            new WorkoutController(workoutService, workoutView, loggedUser);
            workoutView.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error abriendo workouts: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openHistorico() {
        try {
            HistoricoService historicoService = new HistoricoService();
            HistoricoView historicoView = new HistoricoView();
            new HistoricoController(historicoService, historicoView, loggedUser);
            historicoView.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error abriendo histórico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exitApp() {
        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Seguro que quieres salir?", "Salir",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
