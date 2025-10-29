package gymapp.model.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Workout implements Serializable {

    private static final long serialVersionUID = 3378306974356352821L;

    private String id;                 // id del documento en Firestore
    private String name;               // nombre del workout
    private String level;              // nivel (guardado como String en Firestore)
    private String videoURL;           // URL del video informativo
    private List<Exercise> exercises;  // lista de ejercicios

    // --- Getters y Setters ---
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public String getVideoURL() {
        return videoURL;
    }
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    // --- Campo derivado: número de ejercicios ---
    public int getExerciseCount() {
        return (exercises != null) ? exercises.size() : 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, videoURL, exercises);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Workout other = (Workout) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(name, other.name)
            && Objects.equals(level, other.level)
            && Objects.equals(videoURL, other.videoURL)
            && Objects.equals(exercises, other.exercises);
    }

    @Override
    public String toString() {
        return "Workout [id=" + id + ", name=" + name + ", level=" + level
            + ", videoURL=" + videoURL + ", exercises=" + exercises + "]";
    }
}
