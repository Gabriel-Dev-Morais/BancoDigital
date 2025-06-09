package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCliente;

public class ContaCorrente extends Conta{

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

    @Override
    public String toString() {
        return "ContaCorrente{\n" +
                "Cliente: " + cliente +
                "Número e Agência: " + numeroEAgencia + "\n" +
                "Senha: '" + senha + "\n" +
                "Saldo: " + saldo + "\n" +
                "Cartões: " + cartoes +
                '}';
    }

}
