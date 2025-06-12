package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCliente;
import jakarta.persistence.Entity;

@Entity
public class ContaCorrente extends Conta{


    public ContaCorrente() {
    }

    public ContaCorrente(Cliente cliente, String senha) {
        super(cliente, senha);
    }

    public void taxaManutencao(){
        if(this.getCliente().getTipoCliente().equals(TipoCliente.COMUM)){
            this.setSaldo(this.getSaldo() - 12.0);
        }
        else {
            if(this.getCliente().getTipoCliente().equals(TipoCliente.SUPER)){
                this.setSaldo(this.getSaldo() - 8.0);
            }
        }

    }

}
