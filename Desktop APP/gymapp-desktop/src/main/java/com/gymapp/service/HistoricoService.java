package com.gymapp.service;

import com.gymapp.model.Historico;
import com.gymapp.model.User;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class HistoricoService {
    private final Firestore db;

    public HistoricoService() {
        this.db = FirebaseConfig.getDB();
    }

    public DocumentReference save(User user, String workoutName, String date,
                                  int estimatedTime, int completionProgress, int totalTime, int level,
                                  DocumentReference workoutRef) throws Exception {

        DocumentReference historicoRef = db.collection("historicos").document();

        Historico entry = new Historico();
        entry.setWorkoutName(workoutName);
        entry.setDate(date);
        entry.setEstimatedTime(estimatedTime);
        entry.setCompletionProgress(completionProgress);
        entry.setTotalTime(totalTime);
        entry.setLevel(level);
        entry.setUserId(db.collection("users").document(user.getLogin()));
        entry.setWorkoutId(workoutRef);

        historicoRef.set(entry).get();
        return historicoRef;
    }

    public void updateCompletion(String historicoId, int completionProgress, int totalTime) throws Exception {
        db.collection("historicos").document(historicoId)
                .update("completionProgress", completionProgress,
                        "totalTime", totalTime)
                .get();
    }

    public List<Historico> findByUser(User user) throws Exception {
        List<Historico> result = new ArrayList<>();

        QuerySnapshot snapshot = db.collection("historicos")
                .whereEqualTo("userId", db.collection("users").document(user.getLogin()))
                .get().get();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Historico h = doc.toObject(Historico.class);
            if (h != null) {
                // asignar el id del documento para poder ordenar
                h.setId(doc.getId());
                result.add(h);
            }
        }

        // Ordenar por id descendente (últimos creados primero)
        result.sort((a, b) -> b.getId().compareTo(a.getId()));

        return result;
    }
}
