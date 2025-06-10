package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCliente;
import jakarta.persistence.Entity;

@Entity
public class ContaPoupanca extends Conta{

    public ContaPoupanca() {
    }

    public ContaPoupanca(Cliente cliente, String senha) {
        super(cliente, senha);
    }

    public void taxaRendimento(){

        if(this.getCliente().getTipoCliente().equals(TipoCliente.COMUM)){
            this.setSaldo(this.getSaldo() + (this.getSaldo() * 0.5));
        }
        else {
            if(this.getCliente().getTipoCliente().equals(TipoCliente.SUPER)){
                this.setSaldo(this.getSaldo() + (this.getSaldo() * 0.7));
            }
            else {
                this.setSaldo(this.getSaldo() + (this.getSaldo() * 0.9));
            }
        }

    }
}
