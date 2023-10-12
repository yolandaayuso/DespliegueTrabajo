package edu.uclm.esi.ds.Games.services;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import edu.uclm.esi.ds.Games.auxi.HttpClient;
import edu.uclm.esi.ds.Games.dao.TokenDAO;
import edu.uclm.esi.ds.Games.dao.UserDAO;
import edu.uclm.esi.ds.Games.entities.Token;
import edu.uclm.esi.ds.Games.entities.User;
import edu.uclm.esi.ds.Games.ws.Manager;

@Service
public class UsersService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TokenDAO tokenDAO;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Transactional
	public void register(String name, String email, String pwd) throws IOException {

		User user = new User();
		Token token = new Token();
		User usNameOld = this.userDAO.findByName(name);
		User usEmailOld = this.userDAO.findByEmail(email);

		if (usNameOld == null) { // si no encuentra al usuario con ese nombre o con ese correo, que lo cree
			if (usEmailOld == null) {
				createUserToken(name, email, pwd, user, token);
			} else if (usEmailOld.getValidationDate() != null) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Debe introducir un correo distinto.");
			} else if (usEmailOld.getValidationDate() == null) { // si el correo ya existe en la bbdd y el nombre no, y
																	// no se ha validado la cuenta, lo elimina el old y
																	// crea nuevo usuario
				cuentasExistentes(name, email, pwd, user, token, usEmailOld);
			}

		} else if (usNameOld.getValidationDate() == null) {
			// controla que si una persona introduce los email y correo igual y la old no ha
			// validado, borra la old y crea usuario con esos datos
			if (usEmailOld == null) {
				cuentasExistentes(name, email, pwd, user, token, usNameOld);
			} else if (usEmailOld != null && usEmailOld.getValidationDate() != null) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Debe introducir un correo distinto.");
			}

		} else if (usNameOld.getName() != null || usEmailOld.getEmail() != null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Debe introducir un nombre distinto.");
		}
	}
	
	@Transactional
	public void recuperarcontraseña(String name, String pwd) throws IOException {

			try {
				User u = this.userDAO.findByName(name);
				 u.setPwd(passwordEncoder.encode(pwd));
				this.userDAO.updateUserpwd(u.getName(),u.getPwd());
			}catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "No puede realizarse el cambio de contraseña");
			}


	}

	private void cuentasExistentes(String name, String email, String pwd, User user, Token token, User usOld) throws IOException {
		Optional<Token> userToken = this.tokenDAO.findByUserId(usOld.getId());
		Token user_token_old = userToken.get();
		this.tokenDAO.delete(user_token_old);
		this.userDAO.delete(usOld);
		removeUserToken(usOld, user_token_old);
		User usNameNew = this.userDAO.findByName(name);
		if (usNameNew == null) // cuando compruebe que efecitvamente ese usuario ya no esta en la bbdd, que me lo cree
			createUserToken(name, email, pwd, user, token);
	}

	private void createUserToken(String name, String email, String pwd, User user, Token token) throws IOException {
		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd);
		user.setRole("ROLE_USER");
		user.setVip(false);
		token.setUser(user);
		this.userDAO.save(user);
		this.tokenDAO.save(token);
		this.sendEmail(user, token);
	}

	private void removeUserToken(User us, Token token2) {
		this.tokenDAO.delete(token2);
		this.userDAO.delete(us);
	}

	public User login(String name, String pwd) {
		User user = this.userDAO.findByName(name);
		if (user == null)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas.");
		if (user != null && user.getValidationDate() == null)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Credenciales inválidas");
		if (passwordEncoder.matches(pwd, user.getPwd()) == false)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas.");
		return this.userDAO.findByNameAndPwd(name, user.getPwd());
	}

	@Transactional
	public void setVipUser(String userName) {
		User u = this.userDAO.findByName(userName);
		if(u != null) {
			System.out.println(u.getName()+" "+u.getId());
			if(!u.isVip()) {
				u.setVip(true);
				this.userDAO.updateUser(u.getId());
			}
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario incorrecto");
		}
	}
	
	public boolean isVipUser(String userName) {
		boolean result = false;
		User u = this.userDAO.findByName(userName);
		if(u != null) {
			result = u.isVip();
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario incorrecto");
		}
		return result;
	}

	public void confirm(String tokenId) {
		Optional<Token> optToken = this.tokenDAO.findById(tokenId);
		if (!optToken.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el token o ha caducado");
		}
		Token token = optToken.get();
		long time = System.currentTimeMillis();
		if (time - token.getCreationTime() > 6000 * 1000) {
			try {
				User user = token.getUser();
				this.tokenDAO.delete(token);
				this.userDAO.delete(user);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Error al eliminar el token o el usuario", e);
			}
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el token o ha caducado");
		} else {
			User user = token.getUser();
			user.setValidationDate(time);
			this.userDAO.save(user);
			this.tokenDAO.delete(token);
		}

	}

	private void sendEmail(User user, Token token) throws IOException {
		String body = Manager.get().readFile("welcome.html.txt");
		body = body.replace("#TOKEN#", token.getId());
		body = body.replace("#NAME#", user.getName());
		JSONObject emailParameters = new JSONObject(Manager.get().readFile("sendinblue.parameters"));
		String endPoint = emailParameters.getString("endpoint");
		JSONArray headers = emailParameters.getJSONArray("headers");
		JSONObject payload = new JSONObject();
		payload.put("sender", emailParameters.getJSONObject("sender"));
		JSONArray to = new JSONArray();
		to.put(new JSONObject().put("email", user.getEmail()).put("name", user.getName()));
		payload.put("to", to);
		payload.put("subject", emailParameters.getString("subject"));
		payload.put("htmlContent", body);
		HttpClient client = new HttpClient();
		client.sendPost(endPoint, headers, payload);
	}
}