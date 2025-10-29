package com.gymapp.service;

import com.gymapp.model.Set;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SetService {

    private final Firestore db;

    public SetService(Firestore db) {
        this.db = db;
    }

    public Set getByRef(DocumentReference ref) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = ref.get().get();
        if (doc.exists()) {
            Set set = doc.toObject(Set.class);
            if (set != null) {
                set.setId(doc.getId());
            }
            return set;
        }
        return null;
    }

    public List<Set> findByExercise(DocumentReference exerciseRef) throws ExecutionException, InterruptedException {
        List<Set> sets = new ArrayList<>();

        QuerySnapshot snapshot = db.collection("sets")
            .whereEqualTo("exerciseId", exerciseRef)
            .get().get();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Set set = doc.toObject(Set.class);
            if (set != null) {
                set.setId(doc.getId());
                sets.add(set);
            }
        }

        return sets;
    }
}
