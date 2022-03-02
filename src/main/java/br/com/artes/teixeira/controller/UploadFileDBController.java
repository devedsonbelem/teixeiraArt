package br.com.artes.teixeira.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.artes.teixeira.entity.UploadImagemDB;
import br.com.artes.teixeira.repository.UploadImagemRepository;



@ResponseBody
@RestController 
@CrossOrigin("*")
@RequestMapping("/db")
public class UploadFileDBController {
 
	@Autowired 
	UploadImagemRepository imageRepository;
	
	
	@PostMapping ("/upload") 
	public ResponseEntity<?> uploadImage (@RequestParam ("imageFile") MultipartFile file) throws IOException {
		System.out.println ("Tamanho do byte da imagem original -" + file.getBytes() ); 
		UploadImagemDB img = new UploadImagemDB(file.getOriginalFilename (), file.getContentType (), 
				compressBytes (file.getBytes ())); 
		imageRepository.save (img); 
		return ResponseEntity.status (200).body(img); 
	}
	
	
	@GetMapping ("/busca/{imageName}") 
	public UploadImagemDB getImage (@PathVariable ("imageName") String imageName) throws IOException {
		final  UploadImagemDB   retrievedImage = imageRepository.findByName (imageName); 
		UploadImagemDB img = new UploadImagemDB (retrievedImage.getName(), retrievedImage.getTipo(), 
				decompressBytes (retrievedImage.getPicByte())); 
		return img; 
	}



	   
	  public static byte [] compressBytes (byte [] dados) { 
		  ByteArrayOutputStream outputStream = null;
		  try {
		Deflater deflater = new Deflater (); 
		deflater.setInput (dados); 
		deflater.finish ();
	    outputStream = new ByteArrayOutputStream (dados.length); 
		byte [] buffer = new byte [1024]; 
		while (! deflater.finished ()) { 
			int count = deflater.deflate (buffer); 
			outputStream.write (buffer, 0, count ); 
		} 
			outputStream.close (); 
		} catch (IOException e) { 
		} 
		System.out.println ("Tamanho do byte da imagem compactada -" + outputStream.toByteArray (). length);
		return outputStream.toByteArray (); 
	}
	  
	// descompacta os bytes da imagem antes de retorná-los ao aplicativo angular
 	public static byte [] decompressBytes (byte [] dados) {
 		try { 
		Inflater inflater = new Inflater (); 
		inflater.setInput (dados); 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream (dados.length); 
		byte [] buffer =new byte [1024]; 
	
			while (! inflater.finished ()) { 
				int count = inflater.inflate (buffer); 
				outputStream.write (buffer, 0, count); 
			} 
			outputStream.close (); 
			return outputStream.toByteArray (); 
		} catch (Exception ioex) {
 	      return null;
 	   }
 
	} 

}