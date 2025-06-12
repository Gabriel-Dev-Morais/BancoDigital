package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    Optional<Cartao> findByNumero(String numero);
    Boolean existsByNumero(String numero);
    void deleteByNumero(String numero);

    List<Cartao> findByContaClienteCpfAndAtivoTrue(String cpf);

}
