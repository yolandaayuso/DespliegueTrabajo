package edu.uclm.esi.ds.Games.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import edu.uclm.esi.ds.Games.domain.Match;
import jakarta.servlet.http.HttpSession;

@Component
public class Manager {
	
	public ConcurrentHashMap<String, HWSession> sessionsByUserId;
	private ConcurrentHashMap<String, HWSession> sessionsByHttpId;
	private ConcurrentHashMap<String, HWSession> sessionsByWsId;
	private ConcurrentHashMap<String, Match> matches;
	
	private Manager() {
		this.sessionsByUserId = new ConcurrentHashMap<>();
		this.sessionsByHttpId = new ConcurrentHashMap<>();
		this.sessionsByWsId = new ConcurrentHashMap<>();
		this.matches = new ConcurrentHashMap<>();
	}

	private static class ManagerHolder {
		static Manager singleton = new Manager();
	}

	@Bean
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public String readFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
			byte[] b = new byte[fis.available()];
			fis.read(b);
			return new String(b);
		}
	}
	
	
	public void addSessionByUserId(String userId, HttpSession httpSession) {
		HWSession hwSession = new HWSession(httpSession);
		this.sessionsByUserId.put(userId, hwSession);
		this.sessionsByHttpId.put(httpSession.getId(), hwSession);
	}

	public void setWebsocketSession(String httpSessionId, WebSocketSession websocketSession) {
		HWSession hwSession = this.sessionsByHttpId.get(httpSessionId);
		hwSession.setWebsocketSession(websocketSession);
		this.sessionsByWsId.put(websocketSession.getId(), hwSession);
	}

	public HWSession getSessionByUserId(String userId) {
		return this.sessionsByUserId.get(userId);
	}

	public HWSession removeSessionByUserId(String userId) {
		HWSession hwSession = this.sessionsByUserId.remove(userId);
		this.sessionsByHttpId.remove(hwSession.getHttpSession().getId());
		if (hwSession.getWebsocketSession() != null)
			this.sessionsByWsId.remove(hwSession.getWebsocketSession().getId());
		return hwSession;
	}

	public HWSession getSessionByHttpId(String httpId) {
		return this.sessionsByHttpId.get(httpId);
	}

	public HWSession getSessionByWebSocketId(String wsId) {
		return this.sessionsByWsId.get(wsId);
	}

	public HWSession removeSessionByHttpId(String httpId) {
		HWSession hwSession = this.sessionsByHttpId.remove(httpId);
		this.sessionsByUserId.remove(hwSession.getUserId());
		if (hwSession.getWebsocketSession() != null)
			this.sessionsByWsId.remove(hwSession.getWebsocketSession().getId());
		return hwSession;
	}

	public void invalidate(HWSession existingSession) {
		existingSession.getHttpSession().invalidate();
		try {
			existingSession.getWebsocketSession().close();
		} catch (IOException e) {
		}
		this.removeSessionByHttpId(existingSession.getHttpSession().getId());
	}

	public void add(Match match) {
		this.matches.put(match.getId(), match);
	}

	public Match getMatch(String matchId) {
		return this.matches.get(matchId);
	}

	public Match removeMatch(String matchId) {
		return this.matches.remove(matchId);
	}
}