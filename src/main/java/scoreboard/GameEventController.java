package scoreboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/gameEvent")
public class GameEventController {
    @Autowired
    GameEventService gameEventService;

    // http://localhost:8080/gameEvent/getByGameId?gameId=1549&excludePossessionEnded=true
    @CrossOrigin
    @GetMapping(path="/getByGameId")
    public @ResponseBody
    String getByGameId(@RequestParam String gameId, @RequestParam Boolean excludePossessionEnded) throws JsonProcessingException {
        String response = JsonUtil.getJsonList(gameEventService.getByGameId(Integer.parseInt(gameId), excludePossessionEnded));
        return response;
    }
}
