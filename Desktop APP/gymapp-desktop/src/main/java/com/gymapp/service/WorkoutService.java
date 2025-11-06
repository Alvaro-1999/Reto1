package com.gymapp.service;

import com.gymapp.model.Workout;
import com.gymapp.model.Set;
import com.gymapp.model.Exercise;
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
        List<Workout> result = new ArrayList<>();
        for (Workout w : findAll()) {
            if (w.getLevel() <= userLevel) {
                result.add(w);
            }
        }
        return result;
    }

    public List<Set> getSetsFromWorkout(Workout workout) throws Exception {
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
