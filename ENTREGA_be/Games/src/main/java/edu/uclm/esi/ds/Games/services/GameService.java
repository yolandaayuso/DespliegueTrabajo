package edu.uclm.esi.ds.Games.services;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.Games.dao.MatchDAO;
import edu.uclm.esi.ds.Games.dao.MovementDAO;
import edu.uclm.esi.ds.Games.dao.UserDAO;
import edu.uclm.esi.ds.Games.domain.Board;
import edu.uclm.esi.ds.Games.domain.Match;
import edu.uclm.esi.ds.Games.domain.MultiMatch;
import edu.uclm.esi.ds.Games.domain.SingleMatch;
import edu.uclm.esi.ds.Games.domain.WaitingRoom;
import edu.uclm.esi.ds.Games.entities.User;
import edu.uclm.esi.ds.Games.ws.Manager;
import edu.uclm.esi.ds.Games.entities.MovementEntity;
import edu.uclm.esi.ds.Games.entities.MultiMatchEntity;;

@Service
public class GameService {
	@Autowired
	private MatchDAO matchDAO;
	@Autowired
	private MovementDAO movementDAO;
	@Autowired
	private UserDAO userDAO;

	private WaitingRoom waitingRoom;
	private ConcurrentHashMap<String, Match> matches;

	public GameService() {
		this.waitingRoom = new WaitingRoom();
		this.matches = new ConcurrentHashMap<>();
	}

	public Match requestPaidGame(String juego, User player) {
		MultiMatch match = null;
		match = (MultiMatch) this.waitingRoom.findPaidMatch(juego, player);
		if (match.isReady()) {
			this.matches.put(match.getId(), match);
			MultiMatchEntity multiMatchEntity = new MultiMatchEntity();
			multiMatchEntity.setId(match.getId());
			multiMatchEntity.setPlayer1(match.getPlayers().get(0));
			multiMatchEntity.setPlayer2(match.getPlayers().get(1));
			multiMatchEntity.setWinner(match.getWinner());
			this.matchDAO.save(multiMatchEntity);
		}
		Manager.get().add(match);
		return match;
	}

	public Match requestGame(String juego, String player) {
		Match match = null;
		match = new SingleMatch();
		((SingleMatch) match).addPlayer(player);
		if (match.isReady()) {
			this.matches.put(match.getId(), match);
		}
		return match;
	}

	public Match matchNumbers(String id, int x1, int y1, int x2, int y2) throws Exception {
		return ((SingleMatch) this.matches.get(id)).matchNumbers(x1, y1, x2, y2);
	}

	public Match addRows(String id) {
		return ((SingleMatch) this.matches.get(id)).addRows();
	}

	public Board multiMatchNumbers(String id, User player, int x1, int y1, int x2, int y2) throws Exception {
		Optional<MultiMatchEntity> existingMultimatchOptional = this.matchDAO.findById(id);
		Board board = null;
		if (existingMultimatchOptional.isPresent()) {
			
			MultiMatch multiMatch = (MultiMatch) this.matches.get(id);
			board = multiMatch.matchNumbers(player, x1, y1, x2, y2);
			MultiMatchEntity existingMultimatch = existingMultimatchOptional.get();
			MovementEntity movement = new MovementEntity();
			movement.setPlayer(player);
			movement.setMovement(String.format("(%d, %d) - (%d, %d)", x1, y1, x2, y2));
			movement.setMultiMatch(existingMultimatch);
			
			this.movementDAO.save(movement);
			
			if (multiMatch.getWinner() != null) {
				User winner = multiMatch.getWinner();
				existingMultimatch.setWinner(this.userDAO.findByName(winner.getName()));
				existingMultimatch.setLoser(this.userDAO.findById(multiMatch.selectPlayer(winner)).get()); 
				this.matchDAO.save(existingMultimatch);
			}
		} else {
			throw new Exception("La partida no se ha encontrado");
		}
		return board;
	}

	public Board multiAddRows(String id, User player) {
		return ((MultiMatch) this.matches.get(id)).addRows(player);
	}

	public void deleteMatch(String matchId) {
		this.matches.remove(matchId);
	}
}