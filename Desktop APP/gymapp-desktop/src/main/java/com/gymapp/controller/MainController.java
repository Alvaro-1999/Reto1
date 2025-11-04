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
    private HistoricoController historicoController;
    private HistoricoView historicoView;


    public MainController(MainView view, User loggedUser) {
        this.view = view;
        this.loggedUser = loggedUser;

        this.view.lblWelcome.setText("Bienvenido, " + loggedUser.getName() + " (Nivel " + loggedUser.getLevel() + ")");

        this.view.btnWorkouts.addActionListener(e -> openWorkouts());
        this.view.btnHistory.addActionListener(e -> openHistorico());
        this.view.btnExit.addActionListener(e -> exitApp());
    }

    private void openWorkouts() {
        try {
            WorkoutService workoutService = new WorkoutService();
            HistoricoService historicoService = new HistoricoService();

            WorkoutView workoutView = new WorkoutView();
            historicoView = new HistoricoView(); 
            historicoController = new HistoricoController(historicoService, historicoView, loggedUser); // guardar controlador

            new WorkoutController(workoutService, workoutView, loggedUser, historicoController);

            workoutView.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error abriendo workouts: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void openHistorico() {
        try {
            if (historicoController == null || historicoView == null) {
                HistoricoService historicoService = new HistoricoService();
                historicoView = new HistoricoView();
                historicoController = new HistoricoController(historicoService, historicoView, loggedUser);
            } else {
                historicoController.loadHistorico();
            }

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
