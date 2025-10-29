package com.gymapp.service;

import com.gymapp.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class UserResource {
    private final Firestore db;

    public UserResource() {
        this.db = FirebaseConfig.getDB();
    }

    public void save(User user) throws Exception {
        // Guarda el objeto completo, respetando los tipos definidos en User.java
        db.collection("users")
          .document(user.getLogin())
          .set(user)
          .get();
    }

    public User find(User user) throws Exception {
        DocumentSnapshot doc = db.collection("users")
                                 .document(user.getLogin())
                                 .get()
                                 .get();
        if (doc.exists()) {
            User found = doc.toObject(User.class);
            if (found != null) {
                found.setLogin(doc.getId()); // Asegura que el login coincide con el ID del documento
            }
            return found;
        }
        return null;
    }

    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        for (QueryDocumentSnapshot doc : future.get().getDocuments()) {
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setLogin(doc.getId());
                users.add(user);
            }
        }
        return users;
    }

    public void update(User user) throws Exception {
        save(user); // Reutiliza el método save para actualizar
    }

    public void delete(User user) throws Exception {
        db.collection("users")
          .document(user.getLogin())
          .delete()
          .get();
    }
}
