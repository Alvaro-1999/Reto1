package gymapp.model.resource;

import java.io.IOException;
import java.util.List;

import com.google.cloud.firestore.Firestore;
import gymapp.model.Firebase;
import gymapp.model.domain.Exercise;

public class ExerciseResource implements ResourceInterface<Exercise> {

    private final Firestore db;

    public ExerciseResource() throws IOException {
        this.db = Firebase.getInstance().getDb();
    }

    @Override
    public void save(Exercise t) throws Exception {
        // Si decides mantener una colección independiente de ejercicios
        db.collection(gymapp.utils.Constants.EXERCISES_COLLECTION)
          .document(t.getId())
          .set(t)
          .get();
    }

    @Override
    public Exercise find(Exercise t) throws Exception {
        return db.collection(gymapp.utils.Constants.EXERCISES_COLLECTION)
                 .document(t.getId())
                 .get()
                 .get()
                 .toObject(Exercise.class);
    }

    @Override
    public List<Exercise> findAll() throws Exception {
        // Si mantienes la colección, aquí devuelves todos los ejercicios
        return db.collection(gymapp.utils.Constants.EXERCISES_COLLECTION)
                 .get()
                 .get()
                 .toObjects(Exercise.class);
    }

    @Override
    public void update(Exercise t) throws Exception {
        save(t); // set() sobreescribe
    }

    @Override
    public void delete(Exercise t) throws Exception {
        db.collection(gymapp.utils.Constants.EXERCISES_COLLECTION)
          .document(t.getId())
          .delete()
          .get();
    }
}
