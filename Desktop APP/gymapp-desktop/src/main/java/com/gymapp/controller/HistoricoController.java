package com.gymapp.controller;

import com.gymapp.model.User;
import com.gymapp.model.Historico;
import com.gymapp.service.HistoricoService;
import com.gymapp.view.HistoricoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class HistoricoController {
    private final HistoricoService historicoService;
    private final HistoricoView view;
    private final User loggedUser;

    public HistoricoController(HistoricoService historicoService, HistoricoView view, User loggedUser) {
        this.historicoService = historicoService;
        this.view = view;
        this.loggedUser = loggedUser;

        loadHistorico();
        this.view.btnClose.addActionListener(e -> view.dispose());
    }

    public void loadHistorico() {
        try {
            List<Historico> historicoList = historicoService.findByUser(loggedUser);

            String[] columnNames = {"Workout", "Fecha", "Nivel", "Tiempo estimado", "Progreso (%)"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (Historico h : historicoList) {
                String tiempoEstimado = formatTime(h.getEstimatedTime());

                Object[] row = {
                        h.getWorkoutName(),
                        h.getDate(),
                        h.getLevel(),
                        tiempoEstimado,
                        h.getCompletionProgress() + "%"
                };
                model.insertRow(0, row);
            }

            view.tableHistorico.setModel(model);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando histórico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
