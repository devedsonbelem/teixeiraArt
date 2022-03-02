package br.com.artes.teixeira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.artes.teixeira.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
	  public Users findByEmailAndPassword(String email, String password);
      public Users findByEmail(String email);
}
