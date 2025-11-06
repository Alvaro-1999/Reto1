package com.gymapp.model;

import com.google.cloud.firestore.DocumentReference;
import java.io.Serializable;
import java.util.Objects;

public class Historico implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String workoutName;
    private int level;
    private int estimatedTime;
    private int totalTime;
    private int completionProgress;
    private String date;
    private DocumentReference userId;
    private DocumentReference workoutId;

    public Historico() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }

    public int getTotalTime() { return totalTime; }
    public void setTotalTime(int totalTime) { this.totalTime = totalTime; }

    public int getCompletionProgress() { return completionProgress; }
    public void setCompletionProgress(int completionProgress) { this.completionProgress = completionProgress; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public DocumentReference getUserId() { return userId; }
    public void setUserId(DocumentReference userId) { this.userId = userId; }

    public DocumentReference getWorkoutId() { return workoutId; }
    public void setWorkoutId(DocumentReference workoutId) { this.workoutId = workoutId; }

    @Override
    public int hashCode() {
        return Objects.hash(id, workoutName, level, estimatedTime, totalTime, completionProgress, date, userId, workoutId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Historico other = (Historico) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(workoutName, other.workoutName)
            && level == other.level
            && estimatedTime == other.estimatedTime
            && totalTime == other.totalTime
            && completionProgress == other.completionProgress
            && Objects.equals(date, other.date)
            && Objects.equals(userId, other.userId)
            && Objects.equals(workoutId, other.workoutId);
    }

    @Override
    public String toString() {
        return "Historico [id=" + id + ", workoutName=" + workoutName + ", level=" + level + ", estimatedTime=" + estimatedTime
            + ", totalTime=" + totalTime + ", completionProgress=" + completionProgress + ", date=" + date
            + ", userId=" + userId + ", workoutId=" + workoutId + "]";
    }
}
