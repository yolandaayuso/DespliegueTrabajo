package edu.uclm.esi.ds.Games.http;

import java.util.Map;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
@RestController
@RequestMapping("payments")
@CrossOrigin(origins = "http://localhost:4200")

public class PaymentsController {


	static {
		Stripe.apiKey = "sk_test_51MqBSKH7OM2285cjOJ1fGq9jYm8gRG7VnZx2EVtAyYnCB8HyW9kxsrVutq8lJh6UD7J4NTJz8vVSkgyES0OaoBFB001L0pHoHQ";
	}

	@RequestMapping("/prepay")
	public String prepay(@RequestParam double amount) {
		long total = (long) Math.floor(amount * 100);
		PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder().setCurrency("eur").setAmount(total)
				.build();
		try {
			PaymentIntent intent = PaymentIntent.create(params);
			JSONObject jso = new JSONObject(intent.toJson());
			String clientSecret = jso.getString("client_secret");
			// String encripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(clientSecret);
			return clientSecret;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha podido realizar el pago");
		}
	}

	@PostMapping(value = "/paymentOk", consumes = "application/json") // le decimos que solo acepte mensajes con las
																		// cabeceras json
	public void paymentOk(@RequestBody Map<String, String> info) {
		// String token = info.get("token");

		
	}
}

