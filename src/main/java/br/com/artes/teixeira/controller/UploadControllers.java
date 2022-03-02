package br.com.artes.teixeira.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.artes.teixeira.dto.UploadFileResponse;
import br.com.artes.teixeira.entity.UploadFile;
import br.com.artes.teixeira.service.UploadFileService;

@RestController
@ResponseBody
@RequestMapping("/api")
public class UploadControllers {
	@Autowired
	private UploadFileService fileStorageService;

	@Value("${file.upload-dir}")
	private String FILE_DIRECTORY;

	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		UploadFile fileModel = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileModel.getId()).toUriString();
		UploadFileResponse uploadFileResponse = new UploadFileResponse();
		uploadFileResponse.setFileName(fileModel.getFileName());
		uploadFileResponse.setFileType(file.getContentType());
		uploadFileResponse.setSize(file.getSize());
		uploadFileResponse.setFileDownloadUri(fileDownloadUri);
		return uploadFileResponse;
	}

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMulitpleFiles(@RequestParam("files") MultipartFile[] files) {

		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {

		UploadFile fileModel = fileStorageService.getFile(fileId);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileModel.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getFileName() + "\"")
				.body(new ByteArrayResource(fileModel.getData()));
	}

	@PostMapping("/uploadFileOne")
	public ResponseEntity<?> gravar(@RequestParam("File") MultipartFile file) throws IOException {
		try {
			File convertFile = new File(FILE_DIRECTORY + file.getOriginalFilename());
			convertFile.createNewFile();
			FileOutputStream fout = new FileOutputStream(convertFile);
			fout.write(file.getBytes());
			fout.close();
			return new ResponseEntity<Object>("The File Uploaded Successfully.", HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>("Error", HttpStatus.BAD_REQUEST);
		}
	}

}
