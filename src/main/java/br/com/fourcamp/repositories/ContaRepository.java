package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumeroEAgencia(String numeroEAgencia);
    Boolean existsByNumeroEAgencia(String numeroEAgencia);

    void deleteByNumeroEAgencia(String numeroEAgencia);

}
