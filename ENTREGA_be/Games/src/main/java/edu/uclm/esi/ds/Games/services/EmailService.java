package edu.uclm.esi.ds.Games.services;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.uclm.esi.ds.Games.entities.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
/*import okhttp3.Response;*/

@Service
public class EmailService {

	public void sendConfirmationEmail(User user) {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		
		JSONObject jsoSender = new JSONObject()
				.put("name","Juegos S.A")
				.put("email","alejandro.medina6@alu.uclm.es");
		
		JSONObject jsoTo = new JSONObject()
				.put("email", user.getEmail())
				.put("name", user.getName());
		
		JSONArray jsaTo = new JSONArray().put(jsoTo);
		
		JSONObject jsoBody = new JSONObject()
				.put("sender",jsoSender)
				.put("to",jsaTo)
				.put("subject","Bienvenido a los juegos")
				.put("htmlContent","Confirme su cuenta");
		
		
		@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(mediaType, jsoBody.toString());
		
		Request request = new Request.Builder().url("https://api.sendinblue.com/v3/smtp/email").method("POST", body)
				.addHeader("Accept", "application/json")
				.addHeader("api-key",
						"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  ")
				.addHeader("content-type", "application/json")
				.build();
		try {
			/*Response response =*/ client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
