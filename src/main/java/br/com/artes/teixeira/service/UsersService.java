package br.com.artes.teixeira.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.artes.teixeira.entity.Users;
import br.com.artes.teixeira.repository.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UsersRepository dao;

	public void sendEmail(Users u) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(u.getEmail());
		msg.setSubject("Bem vindo a Teixeira's Artes, Obrigado pela escolha");
		msg.setText("Obrigado, agora faz parte de um grande time. " + u.getEmail());
		javaMailSender.send(msg);

	}

	public void sendEmailEsquece(Users u) throws Exception {
		MimeMessage msg = javaMailSender.createMimeMessage();
		Users resp = dao.findByEmail(u.getEmail());
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo(u.getEmail());
		helper.setSubject("Esqueci minha senha");
		helper.setText("<h1>Esqueci minha senha<h1>", true);
		helper.setText( "<br>Clicar para nesse Link <br/><a href='http://enciclopediateixeira.com.br/#/authentication/update/'>"
						+ "Esqueci Minha Senha</a>", true);
		javaMailSender.send(msg);
	}

	public void sendEmailAltera(Users u) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(u.getEmail());
		msg.setSubject("<h2>Senha Alterada Com sucesso <h2>" + u.getEmail());
		javaMailSender.send(msg);

	}

}
