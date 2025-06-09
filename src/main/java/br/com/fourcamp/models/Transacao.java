package br.com.fourcamp.models;

public class Transacao {

    private final Conta contaDestino;
    private final Double valor;

    public Transacao(Conta contaDestino, Double valor) {
        this.contaDestino = contaDestino;
        this.valor = valor;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }


    public Double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "\n\nTransacao{" +
                "Conta Destino: " + contaDestino +
                "Valor: " + valor +
                "}\n";
    }

}
