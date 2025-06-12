package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);
    Boolean existsByCpf(String cpf);

    void deleteByCpf(String cpf);



}
