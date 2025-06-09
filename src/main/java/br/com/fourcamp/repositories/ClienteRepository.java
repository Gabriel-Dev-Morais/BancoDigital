package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByNome(String nome);
    void deleteByNome(String nome);
    Boolean existsByNome(String nome);

}
