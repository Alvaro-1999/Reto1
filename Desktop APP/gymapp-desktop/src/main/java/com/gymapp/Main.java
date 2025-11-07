package com.gymapp;

import com.gymapp.service.BackupManager;
import com.gymapp.service.FirebaseConfig;
import com.gymapp.service.UserService;
 import com.gymapp.view.LoginView;

import java.util.List;

import com.gymapp.backups.BackupService;
import com.gymapp.controller.LoginController;
import com.gymapp.model.Exercise;
import com.gymapp.model.Historico;
import com.gymapp.model.User;
import com.gymapp.model.Workout;

public class Main {
    public static void main(String[] args) throws Exception {
    	 BackupManager backupManager = new BackupManager();
         backupManager.cargarBackupOffline();

         System.out.println("=== Usuarios ===");
         List<User> users = backupManager.getUsers();
         for (User u : users) {
             System.out.println(u.getLogin() + " | " + u.getName() + " | Nivel " + u.getLevel());
         }

         System.out.println("\n=== Workouts ===");
         List<Workout> workouts = backupManager.getWorkouts();
         for (Workout w : workouts) {
             System.out.println(w.getWorkoutName() + " | Nivel " + w.getLevel());
         }

         System.out.println("\n=== Ejercicios y Sets ===");
         List<Exercise> exercises = backupManager.getExercises();
         for (Exercise e : exercises) {
             System.out.println("Ejercicio: " + e.getName() + " | " + e.getDescription());
             List<com.gymapp.model.Set> sets = backupManager.getSets();
             for (com.gymapp.model.Set s : sets) {
                 if (s.getExerciseIdStr().equals("exercises/" + e.getId())) {
                     System.out.println("  Set: " + s.getName() + " | " + s.getTime() + "s");
                 }
             }
         }

         System.out.println("\n=== Históricos ===");
         List<Historico> historicos = backupManager.getHistoricos();
         for (Historico h : historicos) {
        	 System.out.println(
        	            "Workout: " + h.getWorkoutName() +
        	            " | Usuario: " + h.getUserIdStr() +
        	            " | Fecha: " + h.getDate() +
        	            " | Nivel: " + h.getLevel() +
        	            " | Tiempo estimado: " + h.getEstimatedTime() +
        	            " | Tiempo total: " + h.getTotalTime() +
        	            " | Progreso: " + h.getCompletionProgress() +
        	            " | WorkoutRef: " + h.getWorkoutIdStr()
        	    );;
         }

         System.out.println("\n✅ Test offline completado.");
     
        try {
            FirebaseConfig.initFirebase();

            UserService userService = new UserService();
            LoginView loginView = new LoginView();

            new LoginController(userService, loginView);

            loginView.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error iniciando la aplicación: " + e.getMessage());
        }
    }
}
