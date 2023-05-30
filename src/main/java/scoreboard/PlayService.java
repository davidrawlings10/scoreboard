package scoreboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {
    @Autowired public GameService gameService;
    @Autowired public ClockService clockService;
    @Autowired public GameEventService gameEventService;

    public boolean playSec(Game game) throws Exception {
        if (game.isFinal()) {
            game.setStatus(Status.FINAL);
            return true; // return true to signal the end of the game
        }

        if (!game.getClock().getIntermission()) {
            updateScore(game);
        }

        game.getClock().tickDown();

        // save the game every minute
        if (game.getClock().getSeconds() == 0) {
            clockService.save(game.getClock());
        }

        if (game.isFinal()) {
            game.setStatus(Status.FINAL);
            return true;
        }

        game.getClock().handlePeriodEnd();

        System.out.println(printScoreboard(game));

        return false; // return false to signal that the game hasn't ended
    }

    private void updateScore(Game game) throws Exception {
        for (SportEvent sportEvent : SportInfoUtil.getSportInfo(game.getSport()).getSportEvents()) {
            if (RandomUtil.occur(sportEvent.getChanceHome())) {
                incScore(game, sportEvent, true);
                break;
            }
            if (RandomUtil.occur(sportEvent.getChanceAway())) {
                incScore(game, sportEvent, false);
                break;
            }
        }
    }

    public void incScore(Game game, SportEvent sportEvent, boolean isHomeTeam) {
        game.incScore(sportEvent.getScoreAmount(), isHomeTeam);
        gameEventService.save(new GameEvent(game, sportEvent.getEventType(),isHomeTeam));
        gameService.save(game);
        clockService.save(game.getClock());
    }

    public String printScoreboard(Game game) {
        return game.getHomeName() + " | " + game.getHomeScore() + " | " + game.getAwayName() +  " | " + game.getAwayScore() + " | "
                + ((game.getClock().getIntermission() ? "Intermission" : "Period" ) + " | " + game.getClock().getPeriod() + " | " + game.getClock().getMinutes() + ":" + game.getClock().getSeconds());
    }
}
