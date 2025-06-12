package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumeroEAgencia(String numeroEAgencia);
    Boolean existsByNumeroEAgencia(String numeroEAgencia);

    void deleteByNumeroEAgencia(String numeroEAgencia);

}
