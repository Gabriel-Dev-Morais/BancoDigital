package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCartao;
import br.com.fourcamp.enums.TipoCliente;
import br.com.fourcamp.exceptions.SenhaInvalidaException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Random;

@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @ManyToOne
    @JoinColumn(name="conta_id")
    @JsonIgnore
    protected Conta conta;

    @Column(name = "numero_do_cartao")
    protected String numero;

    @Column(name = "senha", length = 4)
    protected String senha;

    @Column(name = "data_de_validade")
    protected LocalDate dataValidade;

    @Column(name = "cvc")
    protected String cvc;

    @Column(name = "ativo")
    protected Boolean ativo;

    @Column(name = "tipo_cartao")
    @Enumerated(value = EnumType.STRING)
    protected TipoCartao tipoCartao;

    @Column(name = "limite")
    protected Double limite;

    public Cartao() {
    }

    public Cartao(Long id, Conta conta, String senha, TipoCartao tipoCartao){

            this.id = id;
            this.conta = conta;
            this.numero = criarNumeroCartao();
            this.senha = senha;
            this.dataValidade = definirDataValidade();
            this.cvc = gerarCvc();
            this.ativo = true;
            this.tipoCartao = tipoCartao;
            definirLimite();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getNumero() {
        return numero;
    }

    public String getSenha() {
        return senha;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public String getCvc() {
        return cvc;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String criarNumeroCartao(){
        Random random = new Random();
        int numero1 = random.nextInt(1000,9000);
        int numero2 = random.nextInt(1000,9000);
        int numero3 = random.nextInt(1000,9000);
        int numero4 = random.nextInt(1000,9000);

        return numero1 + " " +numero2 + " " + numero3 + " " + numero4;
    }

    public LocalDate definirDataValidade(){
        return LocalDate.now().withDayOfMonth(1).plusYears(8);
    }

    public String gerarCvc(){
        Random random = new Random();
        int cvc = random.nextInt(100, 900);
        return String.valueOf(cvc);
    }

    public String validarSenha(String senha) throws SenhaInvalidaException{

            if(senha.matches("\\d{4}")) {
                return senha;
            }
            else {
                throw new SenhaInvalidaException("A senha precisa ter apenas números e 4 caracteres!");
            }
    }


    public void desativarCartao() {
        if (!this.getAtivo()) {
            System.out.println("O cartão já está desativado");
        } else {
            this.setAtivo(false);
            System.out.println("O cartão foi desativado!");
        }
    }

    public void definirLimite(){
        if(this.getConta().getCliente().getTipoCliente() == TipoCliente.COMUM){
            this.setLimite(1000.0);
        }
        else if (this.getConta().getCliente().getTipoCliente() == TipoCliente.SUPER){
            this.setLimite(5000.0);
        }
        else {
            this.setLimite(10000.0);
        }

    }

}
