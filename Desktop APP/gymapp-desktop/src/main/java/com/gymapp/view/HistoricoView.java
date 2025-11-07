package com.gymapp.view;

import javax.swing.*;
import java.awt.*;

public class HistoricoView extends JFrame {
    public JTable tableHistorico;
    public JButton btnClose;

    public HistoricoView() {
        setTitle("Histórico de Workouts");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Nombre", "Fecha", "Nivel", "Tiempo estimado", "Progreso (%)"};

        Object[][] data = {};

        tableHistorico = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(tableHistorico);

        btnClose = new JButton("Cerrar");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);

        add(panel);
    }
}
