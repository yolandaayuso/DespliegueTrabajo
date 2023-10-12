package edu.uclm.esi.ds.Games;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestGames {
	
	// Servidor_falso de_pruebas.
	@Autowired
	private MockMvc server;
	
	@Test
	void testRequestMatchNM() throws Exception {
		String payloadPepe = sendRequest("Pepe");
		JSONObject jsonPepe = new JSONObject(payloadPepe);
		assertFalse(jsonPepe.getBoolean("ready"));
		
		String payloadAna = sendRequest("Ana");
		JSONObject jsonAna = new JSONObject(payloadAna);
		assertTrue(jsonAna.getBoolean("ready"));
		
		assertTrue(jsonAna.getJSONArray("boards").length()==2);
	}

	private String sendRequest(String player) throws Exception, UnsupportedEncodingException {
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestgame?juego=nm&player="+player);
		ResultActions resultActions = this.server.perform(request).andExpect(status().isOk());
		MvcResult result = resultActions.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String payload = response.getContentAsString();
		return payload;
	}
	
	@Test
	void testRequestMatchTrivial() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/games/requestgame?juego=trivial&player=Juan");
		this.server.perform(request).andExpect(status().isNotFound());
	}
	
	

}
