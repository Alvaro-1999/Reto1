package com.gymapp.service;

import com.gymapp.model.*;
import com.google.cloud.firestore.Firestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfflineDataProvider {

    private static final BackupManager backupManager = new BackupManager();

    // Lista de historicos creados offline y pendientes de subir
    private static final List<Historico> historicosPendientes = new ArrayList<>();

    // Bloque estático para cargar los datos al inicio
    static {
        try {
            backupManager.cargarBackupOffline();
            System.out.println("✅ BackupManager inicializado con datos offline.");
            System.out.println("Usuarios cargados: " + backupManager.getUsers().size());
            System.out.println("Workouts cargados: " + backupManager.getWorkouts().size());
            System.out.println("Ejercicios cargados: " + backupManager.getExercises().size());
            System.out.println("Sets cargados: " + backupManager.getSets().size());
            System.out.println("Históricos cargados: " + backupManager.getHistoricos().size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("⚠️ Error cargando backup offline: " + e.getMessage());
        }
    }

    // ================================
    // ===== MÉTODOS GET OFFLINE ======
    // ================================

    public static List<User> getUsers() {
        return backupManager.getUsers();
    }

    public static List<Workout> getWorkoutsForLevel(int level) {
        List<Workout> all = backupManager.getWorkouts();
   

        List<Workout> filtered = all.stream()
                .filter(w -> w.getLevel() <= level)
                .collect(Collectors.toList());

      

        return filtered;
    }

    public static List<Exercise> getExercisesForWorkout(Workout workout) {
        return backupManager.getExercises().stream()
                .filter(e -> e.getWorkoutIdStr() != null &&
                        e.getWorkoutIdStr().equals("workouts/" + workout.getId()))
                .collect(Collectors.toList());
    }

    public static List<com.gymapp.model.Set> getSetsForExercise(Exercise exercise) {
        return backupManager.getSets().stream()
                .filter(s -> s.getExerciseIdStr() != null &&
                        s.getExerciseIdStr().equals("exercises/" + exercise.getId()))
                .collect(Collectors.toList());
    }

    public static List<Historico> getHistoricoForUser(User user) {
        return backupManager.getHistoricos().stream()
                .filter(h -> h.getUserIdStr() != null &&
                        h.getUserIdStr().equals("users/" + user.getLogin()))
                .collect(Collectors.toList());
    }



    public static void guardarHistoricoOffline(Historico h) {
        historicosPendientes.add(h);
        System.out.println("💾 Histórico guardado offline: " + h.getWorkoutName());
    }

    public static void sincronizarConFirestore(Firestore db) throws Exception {
        for (Historico h : historicosPendientes) {
            db.collection("historicos").add(h).get();
        }
        historicosPendientes.clear();
        System.out.println("☁️ Históricos pendientes sincronizados con Firestore.");
    }

    public static boolean tienePendientes() {
        return !historicosPendientes.isEmpty();
    }

    public static List<Historico> getHistoricosPendientes() {
        return new ArrayList<>(historicosPendientes);
    }

    public static void limpiarHistoricosPendientes() {
        historicosPendientes.clear();
    }

    public static void actualizarHistoricoOffline(String historicoId, int completionProgress, int totalTime) {
        for (Historico h : historicosPendientes) {
            if (h.getId() != null && h.getId().equals(historicoId)) {
                h.setCompletionProgress(completionProgress);
                h.setTotalTime(totalTime);
                System.out.println("💾 Histórico offline actualizado: " + h.getWorkoutName());
            }
        }

        List<Historico> all = backupManager.getHistoricos();
        for (Historico h : all) {
            if (h.getId() != null && h.getId().equals(historicoId)) {
                h.setCompletionProgress(completionProgress);
                h.setTotalTime(totalTime);
            }
        }

        try {
            backupManager.guardarBackup();  
            System.out.println("💾 Backup local actualizado tras completar histórico.");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo actualizar backup local: " + e.getMessage());
        }
    }
    public static void subirNivelUsuarioOffline(User user) {
        user.setLevel(user.getLevel() + 1);
        try {
            backupManager.guardarBackup();
            System.out.println("⬆️ Nivel del usuario incrementado y guardado offline.");
        } catch (Exception e) {
            System.err.println("⚠️ Error actualizando nivel offline: " + e.getMessage());
        }
    }  

}
