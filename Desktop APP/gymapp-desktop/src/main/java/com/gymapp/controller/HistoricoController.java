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

    private void loadHistorico() {
        try {
            List<Historico> historicoList = historicoService.findByUser(loggedUser);

            String[] columnNames = {"Workout", "Fecha", "Nivel", "Tiempo estimado", "Progreso (%)", "Tiempo total"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (Historico h : historicoList) {
                Object[] row = {
                        h.getWorkoutName(),
                        h.getDate(),
                        h.getLevel(),
                        h.getEstimatedTime() + " min",
                        h.getCompletionProgress() + "%",
                        h.getTotalTime() + " min"
                };
                model.addRow(row);
            }

            view.tableHistorico.setModel(model);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Error cargando histórico: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
