package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Conta findByNumeroEAgencia(String numeroEAgencia);
    Boolean existsByNumeroEAgencia(String numeroEAgencia);

    void deleteByNumeroEAgencia(String numeroEAgencia);

}
