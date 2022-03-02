package br.com.artes.teixeira.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.artes.teixeira.entity.Users;
import br.com.artes.teixeira.repository.UploadImagemRepository;
import br.com.artes.teixeira.repository.UsersRepository;
import br.com.artes.teixeira.service.UsersService;

@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UsersController {

	@Autowired
	UploadImagemRepository imageRepository;

	@Autowired
	UsersRepository dao;

	@Autowired
	UsersService emailservice;

//	GRAVAR USUARIO
	@PostMapping("/gravar")
	public ResponseEntity<?> gravar(@RequestBody Users usu) {
		try {
			Users resp = usu;
			MessageDigest md = MessageDigest.getInstance("MD5");
			String chave = "profedsonbelem@gmail.comwww.arq.com.br=1=1+123;";
			md.update((resp.getPassword() + chave).getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			String resposta1 = hash.toString(16);
			resp.setPassword(resposta1);

			Users Users = dao.save(usu);
			if (Users == null) {
				throw new Exception("Usuário não gravado");
			}
			Users.setEmail(usu.getEmail());
			emailservice.sendEmail(Users);
			return ResponseEntity.status(200).body(Users);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body("Erro: " + ex.getMessage());
		}
	}

//	APENAS TESTAR SEM GRAVAR
	@PostMapping("/teste")
	public ResponseEntity<?> testeEmail(@RequestBody Users usu) {
		try {
			Users Users = new Users();
			Users.setEmail(usu.getEmail());
			emailservice.sendEmail(Users);
			return ResponseEntity.status(200).body(Users);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body(ex.getMessage());
		}
	}

//	LOGAR COM EMAIL E SENHA
	@PostMapping("/loginmin")
	public ResponseEntity<?> logar(@RequestBody Users usu) {
		try {
			Users resp = usu;
			MessageDigest md = MessageDigest.getInstance("MD5");
			String chave = "profedsonbelem@gmail.comwww.arq.com.br=1=1+123;";
			md.update((resp.getPassword() + chave).getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			String resposta1 = hash.toString(16);

			resp.setPassword(resposta1);
			Users resposta = dao.findByEmailAndPassword(usu.getEmail(), usu.getPassword());
			if (resposta == null) {
				throw new Exception("Email ou senha invalido");
			}
			return ResponseEntity.status(200).body("Logado: " + resposta);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body("Erro: " + ex.getMessage());

		}
	}

	@GetMapping("/buscarId/id/{id}")
	public ResponseEntity<?> buscarId(@PathVariable Long id) {
		Users resp = null;
		try {
			resp = dao.findById(id).get();
			if (resp == null) {
				throw new Exception("Email ou senha nao alterado");
			}
			return ResponseEntity.status(200).body(resp);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body("Erro: " + ex.getMessage());
		}
	}

	@PostMapping(value = "/sendmailcondfirma")
	public String sendmail(@RequestBody Users u) {
		try {
			Users resp = u;
			emailservice.sendEmail(u);
			Users resp2 = dao.findByEmail(u.getEmail());
			return "emailsent:" + resp2;
		} catch (Exception ex) {
			return "error:" + ex.getMessage();
		}
	}

	@GetMapping(value = "/confirmasenha/email/{email}")
	public String sendmailAlteraSenha(@PathVariable String email) {
		try {
			Users resp = dao.findByEmail(email);
			// emailservice.sendEmail(resp);
			Users resp2 = dao.findByEmail(resp.getEmail());
			return "emailsent:" + resp2;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error:" + ex.getMessage();
		}
	}

	@RequestMapping(value = "/createmin", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> createMin(@RequestBody Users Users) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			String chave = "profedsonbelem@gmail.comwww.arq.com.br=1=1+123;";
			md.update((Users.getPassword() + chave).getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			String resposta1 = hash.toString(16);
			Users.setPassword(resposta1);
			Users resposta = dao.save(Users);

			resposta.setEmail(Users.getEmail());
			emailservice.sendEmail(resposta);
			return ResponseEntity.status(200).body(resposta);
		} catch (Exception ex2) {
			ex2.printStackTrace();
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("Users-error", ex2.getMessage());
			mapa.put("status", "nao gravado");
			return ResponseEntity.status(500).body(mapa);
		}

	}

	@RequestMapping(value = "/loginmin", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity<?> loginMin(@RequestBody Users Users) {
		try {
			Users resp = Users;

			MessageDigest md = MessageDigest.getInstance("MD5");
			String chave = "profedsonbelem@gmail.comwww.arq.com.br=1=1+123;";
			md.update((resp.getPassword() + chave).getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			String resposta1 = hash.toString(16);
			resp.setPassword(resposta1);

			Users resposta = dao.findByEmailAndPassword(resp.getEmail(), resp.getPassword());
			if (resposta == null) {
				throw new Exception("Login Invalido");
			}
			return ResponseEntity.status(200).body(resposta);
		} catch (Exception ex2) {
			ex2.printStackTrace();
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("Users-error", ex2.getMessage());
			mapa.put("status", "nao gravado");
			return ResponseEntity.status(500).body(mapa);
		}

	}

	@RequestMapping(value = "/Users/id/{id}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		try {
			Users Users = dao.findById(id).get();
			if (Users == null) {
				throw new IllegalAccessException("nao encontrado");
			}
			return ResponseEntity.status(200).body(Users);
		} catch (Exception ex) {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("error-find", ex.getMessage());
			return ResponseEntity.status(404).body(mapa);
		}
	}

	@RequestMapping(value = "/updateUsers/id/{id}", produces = "application/json", method = RequestMethod.PUT)
	public ResponseEntity<?> updateById(@PathVariable("id") Long id, @RequestBody Users Users) {
		try {
			Users u = dao.findById(id).get();
			if (u == null) {
				throw new IllegalAccessException("nao encontrado");
			}
			u.setEmail(Users.getEmail());
			MessageDigest md = MessageDigest.getInstance("MD5");
			String chave = "profedsonbelem@gmail.comwww.arq.com.br=1=1+123;";
			md.update((Users.getPassword() + chave).getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			String resposta1 = hash.toString(16);
			u.setPassword(resposta1);

			return ResponseEntity.status(200).body(u);
		} catch (Exception ex) {
			ex.printStackTrace();
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("error-find", ex.getMessage());
			return ResponseEntity.status(404).body(mapa);
		}
	}

//	ESQUECEU A SENHA
	@PostMapping("/esqueci")
	public ResponseEntity<?> buscarPorEmail(@RequestBody Users usu) {
		try {
			Users Users = usu;
			Users resposta = dao.findByEmail(usu.getEmail());
			if (resposta == null) {
				throw new Exception("Email não encontrado");
			}
			Users.setIdUsuario(resposta.getIdUsuario());
			Users.setEmail(resposta.getEmail());
			Users.setPassword("-1");
			Users.setUsername(resposta.getUsername());
			Users.setPerfil(resposta.getPerfil());
			Users.setNivel(resposta.getNivel());
			dao.save(Users);
			emailservice.sendEmailEsquece(Users);
			return ResponseEntity.status(200).body(Users);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body("Erro: " + ex.getMessage());
		}
	}

//	ALTERA A SENHA DO EMAIL
	@PutMapping("/alterar/email/{email}")
	public ResponseEntity<?> alterarSenha(@PathVariable String email, @RequestBody Users usu) {
		try {
			Users resp = dao.findByEmail(email);
			Users excluir = new Users();
			excluir.setUsername(resp.getUsername());
			excluir.setEmail(email);
			excluir.setPassword(resp.getPassword());
			excluir.setIdUsuario(resp.getIdUsuario());
			dao.delete(excluir);
			if (resp.getPassword().equals("-1")) {
				String chave = "profedsonbelem@gmail.comwww.arq.com.br=1=1+123;";
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update((usu.getPassword() + chave).getBytes());
				BigInteger hash = new BigInteger(1, md.digest());
				String resposta1 = hash.toString(16);
				resp.setPassword(resposta1);
				resp.setEmail(email);
				dao.save(resp);
			} else {
				throw new Exception("Email ou senha nao alterado");
			}
			return ResponseEntity.status(200).body(usu);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body("Erro: " + ex.getMessage());
		}
	}
}