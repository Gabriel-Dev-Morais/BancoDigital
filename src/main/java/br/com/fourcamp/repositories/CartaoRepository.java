package br.com.fourcamp.repositories;

import br.com.fourcamp.models.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
