package com.gymapp.backups;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BackupLauncher {

    public static void main(String[] args) {
        try {
            // Comando para ejecutar el proceso Java de BackupProcess
            String javaBin = System.getProperty("java.home") + "/bin/java";
            String classpath = System.getProperty("java.class.path");
            String className = "com.gymapp.backups.BackupProcess";

            ProcessBuilder builder = new ProcessBuilder(
                    javaBin, "-cp", classpath, className
            );

            builder.redirectErrorStream(true); // mezclar salida y errores
            Process process = builder.start();

            // Leer salida del proceso hijo
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[BackupProcess] " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("🟢 Proceso de backup finalizado con éxito.");
            } else {
                System.err.println("🔴 El proceso de backup falló (código " + exitCode + ").");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Error lanzando el proceso de backup: " + e.getMessage());
        }
    }
}
