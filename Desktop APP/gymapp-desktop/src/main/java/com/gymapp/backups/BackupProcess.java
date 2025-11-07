package com.gymapp.backups;

import com.gymapp.service.BackupManager;

public class BackupProcess {

    public static void main(String[] args) {
        try {
            System.out.println(" Iniciando proceso de backup...");
            BackupManager manager = new BackupManager();
            manager.guardarBackup();
            System.out.println("✅ Backup completado correctamente.");
        } catch (Exception e) {
            System.err.println("❌ Error durante el backup: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // código 1 = error
        }
        System.exit(0); // código 0 = éxito
    }
}
