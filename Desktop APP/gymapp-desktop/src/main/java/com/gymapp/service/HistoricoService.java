package com.gymapp.service;

import com.gymapp.model.Historico;
import com.gymapp.model.User;
import com.gymapp.util.ConnectionGestor;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class HistoricoService {
    private final Firestore db;

    public HistoricoService() {
        this.db = FirebaseConfig.getDB();
    }

    // Guardar histórico, online o offline
    public DocumentReference save(User user, String workoutName, String date,
                                  int estimatedTime, int completionProgress, int totalTime, int level,
                                  DocumentReference workoutRef) throws Exception {

        Historico entry = new Historico();
        entry.setWorkoutName(workoutName);
        entry.setDate(date);
        entry.setEstimatedTime(estimatedTime);
        entry.setCompletionProgress(completionProgress);
        entry.setTotalTime(totalTime);
        entry.setLevel(level);

        if (ConnectionGestor.hayConexion()) {
            // Online
            entry.setUserId(db.collection("users").document(user.getLogin()));
            entry.setWorkoutId(workoutRef);

            DocumentReference historicoRef = db.collection("historicos").document();
            historicoRef.set(entry).get();
            return historicoRef;
        } else {
            // Offline
            entry.setUserIdStr("users/" + user.getLogin());
            if (workoutRef != null) {
                entry.setWorkoutIdStr("workouts/" + workoutRef.getId());
            }
            OfflineDataProvider.guardarHistoricoOffline(entry);
            System.out.println("⚠️ Guardado offline. Se sincronizará cuando haya conexión.");
            return null; // ID temporal
        }
    }

    // Actualizar progreso
    public void updateCompletion(String historicoId, int completionProgress, int totalTime) throws Exception {
        if (ConnectionGestor.hayConexion()) {
            db.collection("historicos").document(historicoId)
              .update("completionProgress", completionProgress,
                      "totalTime", totalTime)
              .get();
        } else {
            OfflineDataProvider.actualizarHistoricoOffline(historicoId, completionProgress, totalTime);
        }
    }

    // Obtener históricos de un usuario
    public List<Historico> findByUser(User user) throws Exception {
        if (ConnectionGestor.hayConexion()) {
            List<Historico> result = new ArrayList<>();
            QuerySnapshot snapshot = db.collection("historicos")
                    .whereEqualTo("userId", db.collection("users").document(user.getLogin()))
                    .get().get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                Historico h = doc.toObject(Historico.class);
                if (h != null) {
                    h.setId(doc.getId());
                    result.add(h);
                }
            }

            result.sort((a, b) -> b.getId().compareTo(a.getId()));
            return result;
        } else {
            return OfflineDataProvider.getHistoricoForUser(user);
        }
    }

    // Sincronizar históricos pendientes offline -> Firestore
    public void sincronizarConFirestore() throws Exception {
        if (!ConnectionGestor.hayConexion()) return;

        List<Historico> pendientes = OfflineDataProvider.getHistoricosPendientes();
        for (Historico h : pendientes) {
            DocumentReference workoutRef = null;
            if (h.getWorkoutIdStr() != null) {
                workoutRef = db.document(h.getWorkoutIdStr());
            }
            User user = new User();
            user.setLogin(h.getUserIdStr().replace("users/", ""));
            save(user, h.getWorkoutName(), h.getDate(),
                 h.getEstimatedTime(), h.getCompletionProgress(),
                 h.getTotalTime(), h.getLevel(), workoutRef);
        }
        OfflineDataProvider.limpiarHistoricosPendientes();
    }
}
