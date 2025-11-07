package com.gymapp.service;

import com.gymapp.model.Workout;
import com.gymapp.model.Set;
import com.gymapp.model.Exercise;
import com.gymapp.util.ConnectionGestor;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class WorkoutService {

    private final Firestore db;

    public WorkoutService() {
        this.db = FirebaseConfig.getDB();
    }

    public Firestore getDB() {
        return this.db;
    }

    public List<Workout> findAll() throws Exception {
        if (!ConnectionGestor.hayConexion()) {
            return OfflineDataProvider.getWorkoutsForLevel(Integer.MAX_VALUE);
        }

        List<Workout> workouts = new ArrayList<>();
        for (DocumentSnapshot doc : db.collection("workouts").get().get().getDocuments()) {
            Workout workout = doc.toObject(Workout.class);
            if (workout != null) {
                workout.setId(doc.getId());
                workouts.add(workout);
            }
        }
        return workouts;
    }

    public List<Workout> findByLevelOrBelow(int userLevel) throws Exception {
    	System.out.println("Conexión: " + ConnectionGestor.hayConexion());

    	if (!ConnectionGestor.hayConexion()) {
            List<Workout> offlineWorkouts = OfflineDataProvider.getWorkoutsForLevel(userLevel);

            System.out.println("=== Workouts offline encontrados ===");
            for (Workout w : offlineWorkouts) {
                System.out.println("Nombre: " + w.getWorkoutName() + ", Nivel: " + w.getLevel());
            }

            return offlineWorkouts;
        }

        List<Workout> result = new ArrayList<>();
        for (Workout w : findAll()) {
            if (w.getLevel() <= userLevel) {
                result.add(w);
            }
        }
        return result;
    }

    public List<Set> getSetsFromWorkout(Workout workout) throws Exception {
        if (!ConnectionGestor.hayConexion()) {
            List<Exercise> offlineExercises = OfflineDataProvider.getExercisesForWorkout(workout);
            List<Set> offlineSets = new ArrayList<>();
            for (Exercise e : offlineExercises) {
                offlineSets.addAll(OfflineDataProvider.getSetsForExercise(e));
            }
            return offlineSets;
        }

        List<Set> sets = new ArrayList<>();
        DocumentReference workoutRef = db.collection("workouts").document(workout.getId());

        QuerySnapshot exerciseDocs = db.collection("exercises")
                .whereEqualTo("workoutId", workoutRef)
                .get().get();

        for (DocumentSnapshot exerciseDoc : exerciseDocs.getDocuments()) {
            DocumentReference exerciseRef = exerciseDoc.getReference();
            QuerySnapshot setDocs = db.collection("sets")
                    .whereEqualTo("exerciseId", exerciseRef)
                    .get().get();

            for (DocumentSnapshot setDoc : setDocs.getDocuments()) {
                Set set = setDoc.toObject(Set.class);
                if (set != null) {
                    set.setId(setDoc.getId());
                    sets.add(set);
                }
            }
        }

        return sets;
    }

    public List<Exercise> getExercisesFromWorkout(Workout workout) throws Exception {
        if (!ConnectionGestor.hayConexion()) {
            return OfflineDataProvider.getExercisesForWorkout(workout);
        }

        List<Exercise> exercises = new ArrayList<>();
        DocumentReference workoutRef = db.collection("workouts").document(workout.getId());

        QuerySnapshot snapshot = db.collection("exercises")
                .whereEqualTo("workoutId", workoutRef)
                .get().get();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Exercise ex = doc.toObject(Exercise.class);
            if (ex != null) {
                ex.setId(doc.getId());
                exercises.add(ex);
            }
        }
        return exercises;
    }
}
