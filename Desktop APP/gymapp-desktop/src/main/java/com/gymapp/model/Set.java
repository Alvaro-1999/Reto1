package com.gymapp.model;

import com.google.cloud.firestore.DocumentReference;
import java.io.Serializable;
import java.util.Objects;

public class Set implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int reps;
    private int time;
    private DocumentReference exerciseId;

    public Set() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    public DocumentReference getExerciseId() { return exerciseId; }
    public void setExerciseId(DocumentReference exerciseId) { this.exerciseId = exerciseId; }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reps, time, exerciseId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Set other = (Set) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(name, other.name)
            && reps == other.reps
            && time == other.time
            && Objects.equals(exerciseId, other.exerciseId);
    }

    @Override
    public String toString() {
        return "Set [id=" + id + ", name=" + name + ", reps=" + reps
            + ", time=" + time + ", exerciseId=" + exerciseId + "]";
    }
}
