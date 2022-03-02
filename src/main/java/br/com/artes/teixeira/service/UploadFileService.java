package br.com.artes.teixeira.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.artes.teixeira.entity.UploadFile;
import br.com.artes.teixeira.exception.FileStorageException;
import br.com.artes.teixeira.exception.MyFileNotFoundException;
import br.com.artes.teixeira.repository.DBUploadFileRepository;

@Service
public class UploadFileService {
	
	@Autowired 
	DBUploadFileRepository dbFileRepository;
	
	public UploadFile storeFile(MultipartFile file) {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! your file contails invalid characters...");
			}

			UploadFile fileModel = new UploadFile();
			fileModel.setFileName(fileName);
			fileModel.setFileType(file.getContentType());
			fileModel.setData(file.getBytes());

			return dbFileRepository.save(fileModel);

		} catch (IOException ioe) {
			throw new FileStorageException("could not store file " + fileName + " Please try again, " + ioe);
		}

	}

	public UploadFile getFile(Integer fileId) {

		return dbFileRepository.findById(fileId)
				.orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
	}

}
