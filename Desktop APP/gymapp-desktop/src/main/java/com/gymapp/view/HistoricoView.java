package com.gymapp.view;

import javax.swing.*;
import java.awt.*;

public class HistoricoView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTable tableHistorico;
    public JButton btnClose;

    public HistoricoView() {
        setTitle("Histórico de Workouts");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Definir columnas
        String[] columnNames = {"Nombre", "Fecha", "Nivel", "Tiempo estimado", "%", "Tiempo total"};

        // Modelo vacío al inicio
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
