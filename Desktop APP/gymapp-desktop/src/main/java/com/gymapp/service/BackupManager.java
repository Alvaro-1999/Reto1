package com.gymapp.service;

import com.gymapp.model.*;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BackupManager {

    private List<User> users = new ArrayList<>();
    private List<Workout> workouts = new ArrayList<>();
    private List<Exercise> exercises = new ArrayList<>();
    private List<com.gymapp.model.Set> sets = new ArrayList<>();
    private List<Historico> historicos = new ArrayList<>();

    private static final String PATH = "backup/";

    public BackupManager() {
        new File(PATH).mkdirs(); 
    }

    public void cargarBackupOffline() throws IOException, ClassNotFoundException {
        users = cargarArchivo("users.dat", User.class);
        workouts = cargarArchivo("workouts.dat", Workout.class);
        exercises = cargarArchivo("exercises.dat", Exercise.class);
        sets = cargarArchivo("sets.dat", com.gymapp.model.Set.class);
        historicos = cargarArchivo("historicos.dat", Historico.class);

        System.out.println("=== Resumen de carga de archivos ===");
        System.out.println("Usuarios: " + users.size());
        System.out.println("Workouts: " + workouts.size());
        System.out.println("Ejercicios: " + exercises.size());
        System.out.println("Sets: " + sets.size());
        System.out.println("Históricos: " + historicos.size());

        if (FirebaseConfig.getDB() != null) {
            reconstruirReferencias();
            System.out.println("Datos cargados desde backup local y referencias reconstruidas (modo online).");
        } else {
            System.out.println("Datos cargados desde backup local (modo offline, sin reconstruir referencias).");
        }
    }

    public User buscarUsuarioOffline(String login, String password) {
        for (User u : users) {
            if (u.getLogin().equalsIgnoreCase(login)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }


    private <T> List<T> cargarArchivo(String fileName, Class<T> clazz)
            throws IOException, ClassNotFoundException {

        File file = new File(PATH + fileName);
        if (!file.exists()) {
            System.out.println("Archivo no encontrado: " + fileName);
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                List<T> casted = new ArrayList<>();
                for (Object o : list) {
                    if (clazz.isInstance(o)) {
                        casted.add(clazz.cast(o));
                    }
                }
                System.out.println("Archivo " + fileName + " cargado con " + casted.size() + " elementos.");
                return casted;
            }
        }

        System.out.println("Archivo " + fileName + " está vacío o no contiene lista válida.");
        return new ArrayList<>();
    }

    private void reconstruirReferencias() {
        Firestore db = FirebaseConfig.getDB();

        for (Historico h : historicos) {
            if (h.getUserIdStr() != null && h.getUserIdStr().contains("/")) {
                h.setUserId(db.document(h.getUserIdStr()));
            } else {
                h.setUserId(null);
            }

            if (h.getWorkoutIdStr() != null && h.getWorkoutIdStr().contains("/")) {
                h.setWorkoutId(db.document(h.getWorkoutIdStr()));
            } else {
                h.setWorkoutId(null);
            }
        }

        for (Exercise e : exercises) {
            if (e.getWorkoutIdStr() != null && e.getWorkoutIdStr().contains("/")) {
                e.setWorkoutId(db.document(e.getWorkoutIdStr()));
            } else {
                e.setWorkoutId(null);
            }
        }

        for (com.gymapp.model.Set s : sets) {
            if (s.getExerciseIdStr() != null && s.getExerciseIdStr().contains("/")) {
                s.setExerciseId(db.document(s.getExerciseIdStr()));
            } else {
                s.setExerciseId(null);
            }
        }
    }

    public List<User> getUsers() { return users; }
    public List<Workout> getWorkouts() { return workouts; }
    public List<Exercise> getExercises() { return exercises; }
    public List<com.gymapp.model.Set> getSets() { return sets; }
    public List<Historico> getHistoricos() { return historicos; }
    
    public void guardarBackup() throws IOException {
        guardarArchivo("users.dat", users);
        guardarArchivo("workouts.dat", workouts);
        guardarArchivo("exercises.dat", exercises);
        guardarArchivo("sets.dat", sets);
        guardarArchivo("historicos.dat", historicos);
        System.out.println("Backup local guardado correctamente.");
    }

    private void guardarArchivo(String fileName, Object data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATH + fileName))) {
            oos.writeObject(data);
        }
    }

}
