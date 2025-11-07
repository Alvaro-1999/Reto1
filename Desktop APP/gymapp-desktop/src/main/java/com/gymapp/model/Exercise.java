package com.gymapp.model;

import com.google.cloud.firestore.DocumentReference;
import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private int rest;
    private List<Set> sets;

    // DocumentReference en memoria
    private transient DocumentReference workoutId;

    // Para serializar y reconstruir
    private String workoutIdStr;

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRest() { return rest; }
    public void setRest(int rest) { this.rest = rest; }

    public DocumentReference getWorkoutId() { return workoutId; }
    public void setWorkoutId(DocumentReference workoutId) { this.workoutId = workoutId; }

    public String getWorkoutIdStr() { return workoutIdStr; }
    public void setWorkoutIdStr(String workoutIdStr) { this.workoutIdStr = workoutIdStr; }
    public List<Set> getSets() { return sets; }
    public void setSets(List<Set> sets) { this.sets = sets; }
}
