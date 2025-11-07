package com.gymapp.service;

import com.gymapp.model.Exercise;
import com.gymapp.model.Set;
import com.gymapp.util.ConnectionGestor;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class SetService {
  
    private final Firestore db;

    public SetService(Firestore db) {
        this.db = db;
    }

    public Set getByRef(DocumentReference ref) throws Exception {
        if (!ConnectionGestor.hayConexion()) {
            Exercise fake = new Exercise();
            fake.setId(ref.getId());
            List<Set> sets = OfflineDataProvider.getSetsForExercise(fake);
            return sets.isEmpty() ? null : sets.get(0);
        }

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

    public List<Set> findByExercise(DocumentReference exerciseRef) throws Exception {
        if (!ConnectionGestor.hayConexion()) {
            Exercise ex = new Exercise();
            ex.setId(exerciseRef.getId());
            return OfflineDataProvider.getSetsForExercise(ex);
        }

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
