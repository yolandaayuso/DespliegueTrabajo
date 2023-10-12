package edu.uclm.esi.ds.Games.http;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.Games.entities.User;
import edu.uclm.esi.ds.Games.services.UsersService;
import edu.uclm.esi.ds.Games.ws.Manager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UsersController {

	@Autowired
	private UsersService usersService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	int count=0;

	@PostMapping("/register")
	public void register(@RequestBody Map<String, Object> info) { 
		if (!info.get("pwd1").toString().equals(info.get("pwd2").toString()))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden.");
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
		Matcher matcher = pattern.matcher((CharSequence) info.get("email"));
		if (!matcher.find())
		throw new ResponseStatusException(HttpStatus.CONFLICT,
		"El correo suministrado no tiene un formato válido");
		if (info.get("pwd1").toString().length() < 8 || !info.get("pwd1").toString().matches(".*[A-Z].*") || !info.get("pwd1").toString().matches(".*\\d.*") || !info.get("pwd1").toString().matches(".*[^a-zA-Z0-9].*")) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "La contraseña debe tener al menos ocho caracteres, una mayúscula, un número y un símbolo.");
	    }
		try {this.usersService.register(info.get("name").toString(), info.get("email").toString(), passwordEncoder.encode(info.get("pwd1").toString()));} 
		catch (Exception e) {throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());}
	}

	@PutMapping("/login")
	public Map<String, Object> login(HttpServletRequest request, @RequestBody Map<String, Object> info) {
		HttpSession session = request.getSession();
		count+=1;
		
		try {
			User u = this.usersService.login(info.get("name").toString(),info.get("pwd").toString());
			count= 0;
			Manager.get().addSessionByUserId(u.getId(),session);
		} catch (Exception e) {
			if (count ==3) {
				count = 0;
		        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Demasiados intentos de inicio de sesión fallidos. Por favor, espere antes de intentarlo de nuevo.");
		    }
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales invalidas. Te quedan " + ( 3- count) +" intentos" ); 
		}
		return new JSONObject().put("sessionId", session.getId()).toMap();
	}
	
	@PostMapping("/recuperarcontraseña")
	public void recuperarcontraseña(@RequestBody Map<String, Object> info) { 

		if (info.get("pwd").toString().length() < 8 || !info.get("pwd").toString().matches(".*[A-Z].*") || !info.get("pwd").toString().matches(".*\\d.*") || !info.get("pwd").toString().matches(".*[^a-zA-Z0-9].*")) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "La contraseña debe tener al menos ocho caracteres, una mayúscula, un número y un símbolo.");
	    }
		try {this.usersService.recuperarcontraseña(info.get("name").toString(), info.get("pwd").toString());} 
		catch (Exception e) {throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());}
	}

	
	@GetMapping("/vipuser")
	public void vipUser(@RequestParam String userName) {
		this.usersService.setVipUser(userName);
	}
	
	@GetMapping("/isvipuser")
	public ResponseEntity<?>  isVip(@RequestParam String userName) {
        boolean isVipUser = this.usersService.isVipUser(userName);
        
        if (isVipUser) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not a VIP.");
        }

	}

	//SI NO PINCHAN EN EL ENLACE DE CONFIRM, NO SE ELIMINA O SE GUARDA,
	// SIEMPRE SE VA GUARDAR YA QUE LO HACE EN EL USER SERVICE (REGISTER)
	@GetMapping("/confirm/{tokenId}")
	public void confirm(HttpServletResponse response, @PathVariable String tokenId) {
		try {
			this.usersService.confirm(tokenId);
			response.sendRedirect("http://localhost:4200/login");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}