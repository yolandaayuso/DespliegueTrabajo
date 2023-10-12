package edu.uclm.esi.ds.Games.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uclm.esi.ds.Games.domain.MultiMatch;

@Component
public class WSGames extends TextWebSocketHandler {

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String query = session.getUri().getQuery();
		String httpSessionId = query.substring("httpSessionId=".length());
		Manager.get().setWebsocketSession(httpSessionId, session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(message.getPayload().toString());
		String matchId = jsonNode.get("end").asText();
		try {
			((MultiMatch) Manager.get().getMatch(matchId)).notifyEnemyLeave();
		}catch(NullPointerException e) {}
		Manager.get().removeMatch(jsonNode.get("end").asText());
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		HWSession hwSession = Manager.get().getSessionByWebSocketId(session.getId());
		hwSession.setWebsocketSession(null);
		hwSession.setHttpSession(null);
	}
}