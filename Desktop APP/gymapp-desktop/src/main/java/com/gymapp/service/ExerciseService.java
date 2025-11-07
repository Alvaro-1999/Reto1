package com.gymapp.service;

import com.gymapp.model.Exercise;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseService {

    private final Firestore db;

    public ExerciseService(Firestore db) {
        this.db = db;
    }

    public Exercise getByRef(DocumentReference ref) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = ref.get().get();
        if (doc.exists()) {
            Exercise exercise = doc.toObject(Exercise.class);
            if (exercise != null) {
                exercise.setId(doc.getId());
            }
            return exercise;
        }
        return null;
    }

    public List<Exercise> findByWorkout(DocumentReference workoutRef) throws ExecutionException, InterruptedException {
        List<Exercise> exercises = new ArrayList<>();

        QuerySnapshot snapshot = db.collection("exercises")
            .whereEqualTo("workoutId", workoutRef)
            .get().get();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Exercise exercise = doc.toObject(Exercise.class);
            if (exercise != null) {
                exercise.setId(doc.getId());
                exercises.add(exercise);
            }
        }

        return exercises;
    }
}
