package com.gymapp.controller;

import com.gymapp.backups.BackupService;
import com.gymapp.backups.BackupService.BackupData;
import com.gymapp.util.ConnectionGestor;

import javax.swing.*;
import java.util.List;
import com.gymapp.model.Historico;

public class BackupController {

    private final BackupService backupService;

    public BackupController() throws Exception {
        this.backupService = new BackupService();
    }

    /**
     * Descargar todo desde Firestore y guardar backups.
     */
    public void updateBackupFromCloud() {
        try {
            System.out.println("🔄 Descargando datos desde Firestore...");
            backupService.performFullBackup();
            System.out.println("✅ Backup completo generado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar backup: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cargar desde archivos locales (modo offline)
     */
    public BackupData loadBackupOffline() {
        try {
            BackupData data = backupService.restoreAllFromBackup();
            System.out.println("✅ Backup restaurado localmente: users=" + 
                (data.users != null ? data.users.size() : 0) +
                ", workouts=" + (data.workouts != null ? data.workouts.size() : 0));
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar backup local: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Cargar históricos desde XML (offline)
     */
    public List<Historico> loadHistoricosFromXml() {
        try {
            return backupService.loadHistoricosFromXml();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isOnline() {
        return ConnectionGestor.hayConexion();
    }
}
