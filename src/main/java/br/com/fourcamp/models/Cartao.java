package br.com.fourcamp.models;

import br.com.fourcamp.exceptions.SenhaInvalidaException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Random;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @Column(name = "cliente")
    @ManyToOne
    protected Cliente cliente;

    @Column(name="conta")
    @ManyToOne
    protected Conta conta;


    @Column(name = "numero do cartao")
    protected String numero;

    @Column(name = "senha", length = 4)
    protected String senha;

    @Column(name = "data de validade")
    protected LocalDate dataValidade;

    @Column(name = "codigo do cartao")
    protected String cvc;

    @Column(name = "status")
    protected Boolean ativo;

    public Cartao(Long id, Cliente cliente, Conta conta, String senha) throws SenhaInvalidaException {
        this.id = id;
        this.cliente = cliente;
        this.conta = conta;
        this.numero = criarNumeroCartao();
        this.senha = validarSenha(senha);
        this.dataValidade = definirDataValidade();
        this.cvc = gerarCvc();
        this.ativo = true;
    }

    public void setId(Long id) {
        this.id = id;
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


    public void desativarCartão(){
        if (!this.getAtivo()){
            System.out.println("O cartão já está desativado");
        }
        else {
            this.setAtivo(false);
            System.out.println("O cartão foi desativado!");
        }
    }

}
