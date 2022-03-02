package br.com.artes.teixeira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.artes.teixeira.entity.UploadImagemDB;


@Repository
public interface UploadImagemRepository extends JpaRepository<UploadImagemDB, String> {
	 public UploadImagemDB  findByName (String name); 
}


 