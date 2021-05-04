package scoreboard;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Integer sportId, seasonId, homeTeamId, awayTeamId, homeScore, awayScore, endingPeriod;

    @Transient Clock clock;
    @Transient String homeName, awayName;

    public Game() {}

    public Game(int sportId) {
        this.sportId = sportId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Integer getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Integer awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public Integer getEndingPeriod() {
        return endingPeriod;
    }

    public void setEndingPeriod(Integer endingPeriod) {
        this.endingPeriod = endingPeriod;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public boolean isFinal() {
        if (homeScore == null || awayScore == null || clock == null)
            return false;

        return !homeScore.equals(awayScore) && clock.getPeriod() == clock.getENDING_PERIOD() && clock.isPeriodEnded() && !clock.isIntermission // game ends in regulation
                || !homeScore.equals(awayScore) && clock.getPeriod() > clock.getENDING_PERIOD(); // game ends in overtime
    }

    public void increaseHomeScore(int inc) {
        homeScore += inc;
    }

    public void increaseAwayScore(int inc) {
        awayScore += inc;
    }
}