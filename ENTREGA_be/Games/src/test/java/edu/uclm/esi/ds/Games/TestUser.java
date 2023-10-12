package edu.uclm.esi.ds.Games;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class TestUser {
	// Servidor_falso de_pruebas.
	@Autowired
	private MockMvc server;
	
	@Test @Order(1)
	void testRegister() throws Exception{
		ResultActions resultPepe = this.sendRegister("Pepe","pepe@pepe.com","pepe1234","pepe1234");
		resultPepe.andExpect(status().isOk());
		
		ResultActions result1 = this.sendRegister("Pepe","pepe@pepe.com","pepe1234","pepe1234");
		result1.andExpect(status().isConflict());
		
		ResultActions resultAna = this.sendRegister("Ana","ana@ana.com","ana1234","ana1234");
		resultAna.andExpect(status().isOk());
	}
	
	@Test @Order(2)
	void testLogin() throws Exception{
		ResultActions resultPepe = this.sendLogin("Pepe","pepe1234");
		resultPepe.andExpect(status().isOk());
		
		ResultActions resultAna = this.sendLogin("Ana","****");
		resultAna.andExpect(status().isForbidden());
		
		resultAna = this.sendLogin("Ana","ana1234");
		resultAna.andExpect(status().isOk());
	}
	
	private ResultActions sendLogin(String name, String pwd) throws Exception, UnsupportedEncodingException {
		JSONObject jsoUser = new JSONObject()
				.put("name",name)
				.put("pwd",pwd);
		
		RequestBuilder request = MockMvcRequestBuilders
				.put("/users/login")
				.contentType("application/json")
				.content(jsoUser.toString());
		
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}
		
	private ResultActions sendRegister(String name, String email, String pwd1, String pwd2) throws Exception, UnsupportedEncodingException {
		JSONObject jsoUser = new JSONObject()
				.put("name",name)
				.put("email",email)
				.put("pwd1",pwd1)
				.put("pwd2",pwd2);
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/users/register")
				.contentType("application/json")
				.content(jsoUser.toString());
		
		ResultActions resultActions = this.server.perform(request);
		return resultActions;
	}
	
}
