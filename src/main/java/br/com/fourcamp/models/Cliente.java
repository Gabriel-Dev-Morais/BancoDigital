package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCliente;
import br.com.fourcamp.enums.TipoConta;
import br.com.fourcamp.exceptions.CpfInvalidoException;
import br.com.fourcamp.exceptions.DataInvalidaException;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "clientes")
@EqualsAndHashCode
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf", unique = true)
    @NotBlank(message = "Você precisa preencher o campo CPF!")
    @Size(min = 11, max = 11, message = "CPF precisa ter 11 dígitos!")
    private String cpf;

    @Column(name = "data_de_nascimento")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "A data deve estar no passado!")
    private LocalDate dataDeNascimento;

    @Embedded
    @Valid
    private Endereco endereco;

    @Column(name = "tipo_do_cliente")
    @NotNull(message = "É preciso escolher a categoria de cliente que deseja ser (Comum, Super ou Premium)!")
    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    @Column(name = "senhaConta")
    @NotBlank(message = "Precisa ter uma senha para criar a conta!")
    private String senhaConta;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id")
    @NotNull
    private Conta conta;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "tipo_da_conta")
    private TipoConta tipoConta;

    public Cliente() {
    }

    public Cliente(String nome, String cpf, LocalDate dataDeNascimento, Endereco endereco, TipoCliente tipoCliente, String senhaConta, TipoConta tipoConta) {

        try{
            this.nome = validarNome(nome);
            this.cpf = validarCpf(cpf);
            this.dataDeNascimento = validarDataNascimento(dataDeNascimento);
            this.endereco = endereco;
            this.tipoCliente = tipoCliente;
            this.senhaConta = senhaConta;
            this.conta = definirTipoConta(tipoConta);
            this.tipoConta = tipoConta;
        }
        catch (CpfInvalidoException | DataInvalidaException | IllegalArgumentException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf.substring(0,3) + "." +
                cpf.substring(3,6) + "." +
                cpf.substring(6,9) + "-" +
                cpf.substring(9,11);
    }

    public void setCpf(String cpf) throws CpfInvalidoException {
        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            throw new CpfInvalidoException("CPF inválido!");
        }
        this.cpf = cpf;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
        if (this.senhaConta != null) {
            this.conta = definirTipoConta(tipoConta);
        }
    }

    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Conta getConta() {
        return conta;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }
    public String getSenhaConta() {
        return senhaConta;
    }
    public void setSenhaConta(String senhaConta) {
        this.senhaConta = senhaConta;
        if (this.tipoConta != null) {
            this.conta = definirTipoConta(this.tipoConta);
        }
    }

    public String validarNome(String nome){
        if (!nome.matches("[\\p{L} ]{2,100}")){
            throw new IllegalArgumentException("Nome deve ter apenas letras e, pelo menos, 2 caracteres");
        }
        else {
            return nome;
        }
    }
    public String validarCpf(String cpf) throws CpfInvalidoException{
        if(cpf.length() != 11){
            throw new CpfInvalidoException("CPF inválido!");
        }
        else {

            cpf = cpf.substring(0,3) + "." +
                    cpf.substring(3,6) + "." +
                    cpf.substring(6,9) + "-" +
                    cpf.substring(9,11);
            return cpf;
        }
    }
    public LocalDate validarDataNascimento(LocalDate dataNascimento) throws DataInvalidaException{
        LocalDate diaAtual = LocalDate.now();
        int idade = Period.between(dataNascimento, diaAtual).getYears();
        if (idade < 18){
            throw new DataInvalidaException("Você não tem idade o suficiente para ter uma conta!");
        }
        else {
            return dataNascimento;
        }
    }
    public Conta definirTipoConta(TipoConta tipoConta){
        if (tipoConta == TipoConta.CORRENTE){
            return new ContaCorrente(this, this.getSenhaConta());

        }
        else if (tipoConta == TipoConta.POUPANCA){
            return new ContaPoupanca(this, this.getSenhaConta());
        }
        else {
            throw new IllegalArgumentException("Tipo de conta inválido!");
        }
    }
}
