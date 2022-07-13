package model;

public class Train {
    private String trainId;
    private String trainName;
    private String startTime;
    private String EndTime;
    private String trainFrom;
    private String trainTo;


    public Train() { }

    public Train(String trainId, String trainName, String startTime, String endTime, String trainTo, String trainFrom) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.startTime = startTime;
        this.EndTime = endTime;
        this.trainTo = trainTo;
        this.trainFrom = trainFrom;
    }

    public Train(String trainId, String trainName, String startTime, String endTime, Object trainFrom, Object trainto) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.startTime = startTime;
        this.EndTime = endTime;
        this.trainFrom = (String) trainFrom;
        this.trainTo = (String) trainto;
    }


    public String getTrainTo() {
        return trainTo;
    }


    public void setTrainTo(String trainTo) {
        this.trainTo = trainTo;
    }

    public String getTrainFrom() {
        return trainFrom;
    }

    public void setTrainFrom(String trainFrom) {
        this.trainFrom = trainFrom;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", trainName='" + trainName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", trainFrom='" + trainFrom + '\'' +
                ", trainTo='" + trainTo + '\'' +
                '}';
    }
}
