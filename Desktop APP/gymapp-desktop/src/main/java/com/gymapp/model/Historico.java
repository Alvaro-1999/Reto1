package com.gymapp.model;

import com.google.cloud.firestore.DocumentReference;
import java.io.Serializable;

public class Historico implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String workoutName;
    private String date;
    private int estimatedTime;
    private int completionProgress;
    private int totalTime;
    private int level;

    // DocumentReferences en memoria
    private transient DocumentReference userId;
    private transient DocumentReference workoutId;

    // Para serializar y reconstruir
    private String userIdStr;
    private String workoutIdStr;

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }

    public int getCompletionProgress() { return completionProgress; }
    public void setCompletionProgress(int completionProgress) { this.completionProgress = completionProgress; }

    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public DocumentReference getUserId() { return userId; }
    public void setUserId(DocumentReference userId) { this.userId = userId; }

    public DocumentReference getWorkoutId() { return workoutId; }
    public void setWorkoutId(DocumentReference workoutId) { this.workoutId = workoutId; }

    public String getUserIdStr() { return userIdStr; }
    public void setUserIdStr(String userIdStr) { this.userIdStr = userIdStr; }

    public String getWorkoutIdStr() { return workoutIdStr; }
    public void setWorkoutIdStr(String workoutIdStr) { this.workoutIdStr = workoutIdStr; }
}
