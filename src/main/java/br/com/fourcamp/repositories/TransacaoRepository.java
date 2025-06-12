package br.com.fourcamp.repositories;

import br.com.fourcamp.models.CartaoCredito;
import br.com.fourcamp.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByCartaoCreditoNumero(String numero);

}
