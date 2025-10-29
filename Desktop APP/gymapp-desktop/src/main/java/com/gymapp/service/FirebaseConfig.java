package com.gymapp.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static Firestore db;

    public static void initFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount = FirebaseConfig.class.getResourceAsStream("/serviceAccount.json");
            if (serviceAccount == null) {
                throw new IOException("No se encontró serviceAccount.json en resources");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } else {
            db = FirestoreClient.getFirestore();
        }
    }

    public static Firestore getDB() {
        return db;
    }
}
