package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCartao;
import br.com.fourcamp.exceptions.SenhaInvalidaException;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode
public class CartaoDebito extends Cartao{


    public CartaoDebito() {
    }

    public CartaoDebito(Long id, Conta conta, String senha, TipoCartao tipoCartao){
        super(id, conta, senha, tipoCartao);
    }

    public void pagar(Transacao transacao){

        this.getConta().setSaldo(this.getConta().getSaldo() - transacao.getValor());
        transacao.getContaDestino().setSaldo(transacao.getContaDestino().getSaldo() + transacao.getValor());
    }

}
