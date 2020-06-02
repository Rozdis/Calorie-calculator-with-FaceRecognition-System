package facerec;

public class MatchResult {
    private String matchFileName;
    private double matchDistance;
    public MatchResult(String matchFileName,double matchDistance){
        this.matchFileName=matchFileName;
        this.matchDistance=matchDistance;
    }
    public String getMatchFileName() {
        return matchFileName;
    }
    public void setMatchFileName(String matchFileName) {
        this.matchFileName = matchFileName;
    }
    public double getMatchDistance() {
        return matchDistance;
    }
    public void setMatchDistance(double matchDistance) {
        this.matchDistance = matchDistance;
    }
}
