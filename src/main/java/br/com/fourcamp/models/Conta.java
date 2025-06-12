package br.com.fourcamp.models;

import br.com.fourcamp.exceptions.SaldoInsuficienteException;
import br.com.fourcamp.interfaces.OperacoesBancarias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "contas")
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
public class Conta implements OperacoesBancarias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id", unique = true)
    @JsonIgnore
    protected Cliente cliente;

    @Column(name = "numero_e_agencia")
    protected String numeroEAgencia;

    @Column(name = "senha")
    @NotBlank
    @NotNull
    protected String senha;

    @Column(name = "saldo")
    protected Double saldo;

    @Column(name="cartoes")
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    protected List<Cartao> cartoes;

    public Conta() {
    }

    public Conta(Cliente cliente, String senha) {
        this.cliente = cliente;
        this.numeroEAgencia = definirContaEAgencia();
        this.senha = senha;
        this.saldo = 0.0;
        this.cartoes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNumeroEAgencia() {
        return numeroEAgencia;
    }

    public void setNumeroEAgencia(String numeroEAgencia) {
        this.numeroEAgencia = numeroEAgencia;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    public static String definirContaEAgencia(){
        Random random = new Random();
        int numeroConta = random.nextInt(100000);
        int agenciaConta = random.nextInt(100);

        return String.format("%05d-%02d", numeroConta, agenciaConta);
    }


    @Override
    public void exibirSaldo() {
        System.out.println("Saldo atual: "+this.getSaldo());
    }

    @Override
    public void sacar(Double valor) throws SaldoInsuficienteException {
        if(this.getSaldo() >= valor){
            this.setSaldo(this.getSaldo() - valor);
        }
        else {
            throw new SaldoInsuficienteException("Saldo Insuficiente!");
        }
    }

    @Override
    public void depositar(Double valor) {
        this.setSaldo(this.getSaldo() + valor);
    }

    @Override
    public void transferirPix(Double valor, Conta conta) {
        this.setSaldo(this.getSaldo() - valor);
        conta.setSaldo(conta.getSaldo() + valor);
    }

    public void cadastrarCartao(Cartao cartao){
        this.getCartoes().add(cartao);
    }

}
