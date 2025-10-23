package gymapp.model.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;

import gymapp.model.Firebase;
import gymapp.model.domain.Exercise;
import gymapp.model.domain.Workout;
import gymapp.utils.Constants;

public class WorkoutResource implements ResourceInterface<Workout> {

    private final Firestore db;
    private final ObjectMapper mapper;

    public WorkoutResource() throws IOException {
        this.db = Firebase.getInstance().getDb();
        this.mapper = new ObjectMapper();
    }

    @Override
    public void save(Workout workout) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", workout.getId());
        map.put("name", workout.getName());
        map.put("level", workout.getLevel());
        map.put("videoURL", workout.getVideoURL());
        map.put("exercises", workout.getExercises()); // Firestore convierte List<Exercise> en array de mapas

        db.collection(Constants.WORKOUTS_COLLECTION)
          .document(workout.getId())
          .set(map)
          .get();
    }

    @Override
    public Workout find(Workout t) throws Exception {
        DocumentSnapshot doc = db.collection(Constants.WORKOUTS_COLLECTION)
                                 .document(t.getId())
                                 .get()
                                 .get();
        if (!doc.exists()) return null;

        Workout workout = new Workout();
        workout.setId(doc.getId());
        workout.setName(doc.getString("name"));
        workout.setLevel(doc.getString("level"));
        workout.setVideoURL(doc.getString("videoURL"));

        // ejercicios
        List<Map<String, Object>> exList = (List<Map<String, Object>>) doc.get("exercises");
        List<Exercise> exercises = new ArrayList<>();
        if (exList != null) {
            for (Map<String, Object> exMap : exList) {
                Exercise ex = mapper.convertValue(exMap, Exercise.class);
                exercises.add(ex);
            }
        }
        workout.setExercises(exercises);

        return workout;
    }

    @Override
    public List<Workout> findAll() throws Exception {
        List<Workout> ret = new ArrayList<>();

        ApiFuture<QuerySnapshot> future = db.collection(Constants.WORKOUTS_COLLECTION).get();
        List<QueryDocumentSnapshot> workoutDocuments = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : workoutDocuments) {
            Workout workout = new Workout();
            workout.setId(doc.getId());
            workout.setName(doc.getString("name"));
            workout.setLevel(doc.getString("level"));
            workout.setVideoURL(doc.getString("videoURL"));

            // ejercicios
            List<Map<String, Object>> exList = (List<Map<String, Object>>) doc.get("exercises");
            List<Exercise> exercises = new ArrayList<>();
            if (exList != null) {
                for (Map<String, Object> exMap : exList) {
                    Exercise ex = mapper.convertValue(exMap, Exercise.class);
                    exercises.add(ex);
                }
            }
            workout.setExercises(exercises);

            ret.add(workout);
        }

        return ret;
    }

    @Override
    public void update(Workout workout) throws Exception {
        save(workout); // en Firestore, set() sobreescribe el documento
    }

    @Override
    public void delete(Workout workout) throws Exception {
        db.collection(Constants.WORKOUTS_COLLECTION)
          .document(workout.getId())
          .delete()
          .get();
    }
}
