package br.com.fourcamp.models;

import jakarta.persistence.*;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;
    private Double valor;

    public Transacao() {
    }

    public Transacao(Conta contaDestino, Double valor) {
        this.contaDestino = contaDestino;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartaoCredito getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(CartaoCredito cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
