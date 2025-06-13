package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCartao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(value = {"limite"})
public class CartaoDebito extends Cartao{


    public CartaoDebito() {
    }

    public CartaoDebito(Long id, Conta conta, String senha, TipoCartao tipoCartao){
        super(id, conta, senha, tipoCartao);
    }


    public void pagar(Transacao transacao){

        Conta origem = this.getConta();
        Conta destino = transacao.getContaDestino();

        origem.setSaldo(origem.getSaldo() - transacao.getValor());
        destino.setSaldo(destino.getSaldo() + transacao.getValor());
    }

}
