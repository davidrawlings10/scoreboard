package scoreboard;

import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class HockeyPlayService {
    @Autowired private TeamService teamService;

    private static String homeTeamName, awayTeamName;

    public class Config {
        public class Chance {
            // total GA (goals for) in 18-19 NHL regular season was 7664
            // games played in 18-19 NHL regular season was 82 * 31 = 2542
            // 7664 / 2542 = 3.014 goals a game
            // considering overtime goals rounding down to .9 goals a period
            // .9 / 20 = .045 average goals per minutes
            // .045 / 60 = .00075 average goals per second
            public final static double regulationScore = 0.075, regulationScoreHomeWeight = 0.005, regulationScoreAwayWeight = -0.005;
            public final static double overtimeScore = 0.1, overtimeScoreHomeWeight = 0.005, overtimeScoreAwayWeight = -0.005;
            public final static double shootoutScore = 31.94, shootoutScoreHomeWeight = 1, shootoutScoreAwayWeight = -1;
        }
        // time in seconds for how long things take
        public class TimeDelay {
            // public final static int gameplayTickMilli = 1000, shootoutSec = 40; // standard
            public final static int gameplayTickMilli = 25, shootoutSec = 3; // accelerated
            // public final static int gameplayTickMilli = 0, shootoutSec = 0; // immediate
        }
    }

    public Game playGame(Game game) throws InterruptedException {
        game.getClock().reset();

        double homeChance = Config.Chance.regulationScore + Config.Chance.regulationScoreHomeWeight, awayChance = Config.Chance.regulationScore + Config.Chance.regulationScoreAwayWeight;

        homeTeamName = getByTeamId(game.getHomeTeamId()).getName();
        awayTeamName = getByTeamId(game.getAwayTeamId()).getName();

        System.out.println(printScoreboard(game, false));

        while (true) {

            TimeUnit.MILLISECONDS.sleep(Config.TimeDelay.gameplayTickMilli);

            if (game.getClock().getPeriod() == game.getClock().getENDING_PERIOD() + 2 && !game.getClock().isIntermission()) {
                if (shootout()) {
                    game.setHomeScore(game.getHomeScore() + 1);
                } else {
                    game.setAwayScore(game.getAwayScore() + 1);
                }
                break;
            }

            if (!game.getClock().isIntermission() && RandomService.occur(homeChance)) {
                game.setHomeScore(game.getHomeScore() + 1);
                if (game.getClock().getPeriod() == game.getClock().getENDING_PERIOD() + 1)
                    break;
            }
            if (!game.getClock().isIntermission() && RandomService.occur(awayChance)) {
                game.setAwayScore(game.getAwayScore() + 1);
                if (game.getClock().getPeriod() == game.getClock().getENDING_PERIOD() + 1)
                    break;
            }

            game.getClock().tickDown();

            if (game.getClock().isExpired()) {
                // period ends

                System.out.println(printScoreboard(game, false));

                // end of the game in regulation if scores are different, otherwise period will become 4
                if (game.getClock().getPeriod() == game.getClock().getENDING_PERIOD() && !game.getClock().isIntermission() && !game.getHomeScore().equals(game.getAwayScore())) {
                    break;
                } else {
                    if (!game.getClock().isIntermission()) {
                        game.getClock().setPeriod(game.getClock().getPeriod() + 1);
                    }
                    game.getClock().setIntermission(!game.getClock().isIntermission());
                }

                game.getClock().reset();

                // if overtime is starting update increase the chance of a goal as overtime is played 3 on 3
                if (game.getClock().getPeriod() == game.getClock().getENDING_PERIOD() + 1) {
                    homeChance = Config.Chance.overtimeScore + Config.Chance.overtimeScoreHomeWeight;
                    awayChance = Config.Chance.overtimeScore + Config.Chance.overtimeScoreAwayWeight;
                }
            }

            System.out.println(printScoreboard(game, false));
        }

        System.out.println(printScoreboard(game, false));

        game.setEndingPeriod(game.getClock().getPeriod());

        return game;
    }

    private void handleShootout() {

    }

    private void handleScore() {

    }

    private void handleClock() {

    }

    private boolean shootout() throws InterruptedException {
        int homeShootoutScore = 0, awayShootoutScore = 0, shootoutRound = 1;
        while (true) {
            if (RandomService.occur(Config.Chance.shootoutScore + Config.Chance.shootoutScoreHomeWeight)) {
                homeShootoutScore++;
                System.out.println(homeTeamName + " scores");
            } else {
                System.out.println(homeTeamName + " misses");
            }
            TimeUnit.SECONDS.sleep(Config.TimeDelay.shootoutSec);
            if (RandomService.occur(Config.Chance.shootoutScore + Config.Chance.shootoutScoreAwayWeight)) {
                awayShootoutScore++;
                System.out.println(awayTeamName + " scores");
            } else {
                System.out.println(awayTeamName + " misses");
            }
            System.out.println(printScoreboardShootout(homeShootoutScore, awayShootoutScore, shootoutRound));
            TimeUnit.SECONDS.sleep(Config.TimeDelay.shootoutSec);
            if (shootoutRound >= 3 && homeShootoutScore != awayShootoutScore) {
                break;
            }
            shootoutRound++;
        }

        return homeShootoutScore > awayShootoutScore;
    }

    private String printScoreboard(Game game, boolean isFinal) {
        return homeTeamName + " | " + game.getHomeScore() + " | " + awayTeamName +  " | " + game.getAwayScore() + " | "
                + (isFinal ? "Final" : (game.getClock().isIntermission() ? "Intermission" : "Period" ) + " | " + game.getClock().getPeriod() + " | " + game.getClock().getMinutes() + ":" + game.getClock().getSeconds());
    }

    private String printScoreboardShootout(int homeShootoutScore, int awayShootoutScore, int shootoutRound) {
        return homeTeamName + " | " + homeShootoutScore + " | " + awayTeamName +  " | " + awayShootoutScore + " | " +
                "Round | " + shootoutRound;
    }

    public Team getByTeamId(int teamId) { return teamService.getByTeamId(teamId); }
}
