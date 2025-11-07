package com.gymapp.backups;

import com.gymapp.model.*;
import com.gymapp.util.HistoricoXMLService;
import com.gymapp.service.FirebaseConfig;
import com.google.cloud.firestore.*;

import java.io.*;
import java.util.*;

public class BackupService {

    private static final String BACKUP_DIR = "backup/";
    private final Firestore db;
    private final HistoricoXMLService xmlService;

    public BackupService() {
        this.db = FirebaseConfig.getDB();
        this.xmlService = new HistoricoXMLService();
        new File(BACKUP_DIR).mkdirs();
    }

    // ------------------ BACKUP COMPLETO ------------------
    public void performFullBackup() throws Exception {
        BackupData data = new BackupData();

        data.users = downloadUsers();
        data.workouts = downloadWorkouts();
        data.exercises = downloadExercises();
        data.sets = downloadSets();
        data.historicos = downloadHistoricos();

        // Antes de guardar, convertir DocumentReference a String
        convertReferencesToStrings(data);

        saveObject(data.users, BACKUP_DIR + "users.dat");
        saveObject(data.workouts, BACKUP_DIR + "workouts.dat");
        saveObject(data.exercises, BACKUP_DIR + "exercises.dat");
        saveObject(data.sets, BACKUP_DIR + "sets.dat");
        saveObject(data.historicos, BACKUP_DIR + "historicos.dat");

        // Generar XML de historicos
        xmlService.saveHistoricoXML(data.historicos);
        System.out.println("✅ Backup completo realizado.");
    }

    // ------------------ MÉTODOS DE DESCARGA ------------------
    private List<User> downloadUsers() throws Exception {
        List<User> list = new ArrayList<>();
        QuerySnapshot snap = db.collection("users").get().get();
        for (DocumentSnapshot doc : snap.getDocuments()) {
            Map<String, Object> map = doc.getData();
            User u = new User();
            u.setId(doc.getId());
            u.setLogin(safeGetString(map, "login", null));
            u.setName(safeGetString(map, "name", null));
            u.setLastName(safeGetString(map, "lastName", null));
            u.setMail(safeGetString(map, "mail", null));
            u.setPassword(safeGetString(map, "password", null));
            u.setBirthDate(safeGetString(map, "birthDate", null));
            u.setLevel(safeGetInt(map, "level", 0));
            u.setUserType(safeGetString(map, "userType", null));
            list.add(u);
        }
        return list;
    }

    private List<Workout> downloadWorkouts() throws Exception {
        List<Workout> list = new ArrayList<>();
        QuerySnapshot snap = db.collection("workouts").get().get();
        for (DocumentSnapshot doc : snap.getDocuments()) {
            Map<String, Object> map = doc.getData();
            Workout w = new Workout();
            w.setId(doc.getId());
            w.setWorkoutName(safeGetString(map, "workoutName", null));
            w.setVideo(safeGetString(map, "video", null));
            w.setTiempo(safeGetInt(map, "estimatedTime", 0));
            w.setNumEj(safeGetInt(map, "numEj", 0));
            w.setLevel(safeGetInt(map, "level", 0));
            list.add(w);
        }
        return list;
    }

    private List<Exercise> downloadExercises() throws Exception {
        List<Exercise> list = new ArrayList<>();
        QuerySnapshot snap = db.collection("exercises").get().get();
        for (DocumentSnapshot doc : snap.getDocuments()) {
            Map<String, Object> map = doc.getData();
            Exercise e = new Exercise();
            e.setId(doc.getId());
            e.setName(safeGetString(map, "name", null));
            e.setDescription(safeGetString(map, "description", null));
            e.setRest(safeGetInt(map, "rest", 0));

            Object wr = map != null ? map.get("workoutId") : null;
            if (wr != null) {
                e.setWorkoutId(parseDocumentReference(wr));
            }

            list.add(e);
        }
        return list;
    }

    private List<com.gymapp.model.Set> downloadSets() throws Exception {
        List<com.gymapp.model.Set> list = new ArrayList<>();
        QuerySnapshot snap = db.collection("sets").get().get();
        for (DocumentSnapshot doc : snap.getDocuments()) {
            Map<String, Object> map = doc.getData();
            com.gymapp.model.Set s = new com.gymapp.model.Set();
            s.setId(doc.getId());
            s.setName(safeGetString(map, "name", null));
            s.setReps(safeGetInt(map, "reps", 0));
            s.setTime(safeGetInt(map, "time", 0));

            Object er = map != null ? map.get("exerciseId") : null;
            if (er != null) {
                s.setExerciseId(parseDocumentReference(er));
            }

            list.add(s);
        }
        return list;
    }

    private List<Historico> downloadHistoricos() throws Exception {
        List<Historico> list = new ArrayList<>();
        QuerySnapshot snap = db.collection("historicos").get().get();
        for (DocumentSnapshot doc : snap.getDocuments()) {
            Map<String, Object> map = doc.getData();
            Historico h = new Historico();
            h.setId(doc.getId());
            h.setWorkoutName(safeGetString(map, "workoutName", null));
            h.setDate(safeGetString(map, "date", null));
            h.setEstimatedTime(safeGetInt(map, "estimatedTime", 0));
            h.setCompletionProgress(safeGetInt(map, "completionProgress", 0));
            h.setTotalTime(safeGetInt(map, "totalTime", 0));
            h.setLevel(safeGetInt(map, "level", 0));

            Object ur = map != null ? map.get("userId") : null;
            Object wr = map != null ? map.get("workoutId") : null;
            if (ur != null) h.setUserId(parseDocumentReference(ur));
            if (wr != null) h.setWorkoutId(parseDocumentReference(wr));

            list.add(h);
        }
        return list;
    }
 


    // ------------------ MÉTODOS AUXILIARES ------------------

    private DocumentReference parseDocumentReference(Object obj) {
        if (obj instanceof DocumentReference) return (DocumentReference) obj;

        String path = obj.toString();
        if (path.startsWith("DocumentReference{path=")) {
            path = path.substring("DocumentReference{path=".length(), path.length() - 1);
        }
        return db.document(path);
    }

    private void convertReferencesToStrings(BackupData data) {
        // Exercise: workoutId
        for (Exercise e : data.exercises) {
            if (e.getWorkoutId() != null) {
                e.setWorkoutIdStr(e.getWorkoutId().getPath());
            }
        }
        // Set: exerciseId
        for (com.gymapp.model.Set s : data.sets) {
            if (s.getExerciseId() != null) {
                s.setExerciseIdStr(s.getExerciseId().getPath());
            }
        }
        // Historico: userId + workoutId
        for (Historico h : data.historicos) {
            if (h.getUserId() != null) {
                h.setUserIdStr(h.getUserId().getPath());
            }
            if (h.getWorkoutId() != null) {
                h.setWorkoutIdStr(h.getWorkoutId().getPath());
            }
        }
    }

    private static String safeGetString(Map<String, Object> map, String key, String def) {
        if (map == null) return def;
        Object o = map.get(key);
        return o == null ? def : o.toString();
    }

    private static int safeGetInt(Map<String, Object> map, String key, int def) {
        if (map == null) return def;
        Object o = map.get(key);
        if (o instanceof Number) return ((Number) o).intValue();
        try { return Integer.parseInt(o.toString()); } catch (Exception e) { return def; }
    }

    private void saveObject(Object obj, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
        }
    }

    public BackupData restoreAllFromBackup() throws Exception {
        BackupData data = new BackupData();
        data.users = (List<User>) readObject(BACKUP_DIR + "users.dat");
        data.workouts = (List<Workout>) readObject(BACKUP_DIR + "workouts.dat");
        data.exercises = (List<Exercise>) readObject(BACKUP_DIR + "exercises.dat");
        data.sets = (List<com.gymapp.model.Set>) readObject(BACKUP_DIR + "sets.dat");
        data.historicos = (List<Historico>) readObject(BACKUP_DIR + "historicos.dat");

        // Reconstruir DocumentReference desde String
        Firestore db = FirebaseConfig.getDB();
        for (Exercise e : data.exercises) {
            if (e.getWorkoutIdStr() != null) e.setWorkoutId(db.document(e.getWorkoutIdStr()));
        }
        for (com.gymapp.model.Set s : data.sets) {
            if (s.getExerciseIdStr() != null) s.setExerciseId(db.document(s.getExerciseIdStr()));
        }
        for (Historico h : data.historicos) {
            if (h.getUserIdStr() != null) h.setUserId(db.document(h.getUserIdStr()));
            if (h.getWorkoutIdStr() != null) h.setWorkoutId(db.document(h.getWorkoutIdStr()));
        }

        return data;
    }
    public void checkHistoricosDat() throws Exception {
        List<Historico> historicos = (List<Historico>) readObject(BACKUP_DIR + "historicos.dat");

        if (historicos.isEmpty()) {
            System.out.println(" El archivo historicos.dat está vacío o no existe.");
            return;
        }

        System.out.println(" Contenido de historicos.dat:");
        for (Historico h : historicos) {
            System.out.println("ID: " + h.getId());
            System.out.println("UserIdStr: " + h.getUserIdStr());
            System.out.println("WorkoutIdStr: " + h.getWorkoutIdStr());
            System.out.println("--------------------------");
        }
    }
    public List<Historico> loadHistoricosFromXml() throws Exception {
        return xmlService.loadHistoricoXML();
    }

    private Object readObject(String filePath) throws IOException, ClassNotFoundException {
        File f = new File(filePath);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return ois.readObject();
        }
    }

    // ------------------ CLASE INTERNA ------------------
    public static class BackupData implements Serializable {
        public List<User> users = new ArrayList<>();
        public List<Workout> workouts = new ArrayList<>();
        public List<Exercise> exercises = new ArrayList<>();
        public List<com.gymapp.model.Set> sets = new ArrayList<>();
        public List<Historico> historicos = new ArrayList<>();
    }
}
