package br.com.artes.teixeira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.artes.teixeira.entity.UploadFile;


@Repository
public interface DBUploadFileRepository extends JpaRepository<UploadFile, Integer>{
	
	 
}
