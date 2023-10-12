package edu.uclm.esi.ds.Games.http;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.Games.dao.UserDAO;
import edu.uclm.esi.ds.Games.domain.Board;
import edu.uclm.esi.ds.Games.domain.Match;
import edu.uclm.esi.ds.Games.services.GameService;

@RestController
@CrossOrigin
@RequestMapping("games")
public class GamesController {
	
	@Autowired
	private GameService gamesService;
	@Autowired
	private UserDAO userDAO;

	@GetMapping("/requestgame")
	public Match requestGame(@RequestParam String juego, @RequestParam String player) {
		if (!juego.equals("nm")) {
			throw new ResponseStatusException(HttpStatus.SC_NOT_FOUND, "No se encuentra ese juego", null);
		}
		return this.gamesService.requestGame(juego,player);
	}
	
	@GetMapping("/requestpaidgame")
	public Match requestPaidGame(@RequestParam String juego, @RequestParam String player) {
		if (!juego.equals("nm")) {
			throw new ResponseStatusException(HttpStatus.SC_NOT_FOUND, "No se encuentra ese juego", null);
		}
		return this.gamesService.requestPaidGame(juego,this.userDAO.findByName(player));
	}
	
	@PutMapping("/matchnumbers")
	public Match matchNumbers(@RequestBody Map<String,String> info) {
		Match match = null;
		try {
			match = this.gamesService.matchNumbers(info.get("id"), 
					Integer.parseInt(info.get("x1")), 
					Integer.parseInt(info.get("y1")), 
					Integer.parseInt(info.get("x2")), 
					Integer.parseInt(info.get("y2")));
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.SC_NOT_ACCEPTABLE, e.getMessage(), null);
		}
		return match;
	}
	
	@GetMapping("/addrows")
	public Match addRows(@RequestParam String id) {
		return this.gamesService.addRows(id);
	}
	
	@PutMapping("/multimatchnumbers")
	public Board multiMatchNumbers(@RequestBody Map<String,String> info) {
		Board board = null;
		try {
			board = this.gamesService.multiMatchNumbers(info.get("id"),
					this.userDAO.findByName(info.get("player")),
					Integer.parseInt(info.get("x1")), 
					Integer.parseInt(info.get("y1")), 
					Integer.parseInt(info.get("x2")), 
					Integer.parseInt(info.get("y2")));
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.SC_NOT_ACCEPTABLE, e.getMessage(), null);
		}
		return board;
	}
	
	@GetMapping("/multiaddrows")
	public Board multiAddRows(@RequestParam String id, @RequestParam String player) {
		return this.gamesService.multiAddRows(id,this.userDAO.findByName(player));
	}
	
	@GetMapping("/removematch")
	public void removeMatch(@RequestParam String matchId) {
		this.gamesService.deleteMatch(matchId);
	}
	
}