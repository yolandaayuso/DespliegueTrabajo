package edu.uclm.esi.ds.Games.ws;

import org.springframework.web.socket.WebSocketSession;
import jakarta.servlet.http.HttpSession;

public class HWSession {
	private HttpSession httpSession;
	private WebSocketSession websocketSession;

	public HWSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public HWSession(HttpSession httpSession, WebSocketSession websocketSession) {
		this.httpSession = httpSession;
		this.websocketSession = websocketSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setWebsocketSession(WebSocketSession websocketSession) {
		this.websocketSession = websocketSession;
	}

	public WebSocketSession getWebsocketSession() {
		return websocketSession;
	}

	public String getUserId() {
		return this.httpSession.getAttribute("userId").toString();
	}
}