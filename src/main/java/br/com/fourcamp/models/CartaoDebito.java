package br.com.fourcamp.models;

import br.com.fourcamp.exceptions.SenhaInvalidaException;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "debitos")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CartaoDebito extends Cartao{

    public CartaoDebito(Long id, Cliente cliente, Conta conta, String senha) throws SenhaInvalidaException {
        super(id, cliente, conta, senha);
    }

    public void pagar(Transacao transacao){

        this.getConta().setSaldo(this.getConta().getSaldo() - transacao.getValor());
        transacao.getContaDestino().setSaldo(transacao.getContaDestino().getSaldo() + transacao.getValor());
    }

    @Override
    public String toString() {
        return "CartaoDebito{\n" +
                "Número: '" + numero + "\n" +
                "Data de Validade: " + dataValidade + "\n" +
                "Ativo: " + ativo + "\n" +
                "}\n";
    }

}
