package com.gymapp.model;

import com.google.cloud.firestore.DocumentReference;
import java.io.Serializable;

public class Set implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int reps;
    private int time;

    // DocumentReference en memoria
    private transient DocumentReference exerciseId;

    // Para serializar y reconstruir
    private String exerciseIdStr;

    // Getters y setters
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

    public String getExerciseIdStr() { return exerciseIdStr; }
    public void setExerciseIdStr(String exerciseIdStr) { this.exerciseIdStr = exerciseIdStr; }
}
