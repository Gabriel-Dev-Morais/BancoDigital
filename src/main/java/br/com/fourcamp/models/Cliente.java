package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCliente;
import br.com.fourcamp.exceptions.CpfInvalidoException;
import br.com.fourcamp.exceptions.IdadeInvalidaException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "clientes")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "data de nascimento")
    private LocalDate dataDeNascimento;

    @Embedded
    @Column(name = "endereco")
    private Endereco endereco;

    @Column(name = "tipo do cliente")
    private TipoCliente tipoCliente;


    public Cliente(String nome, String cpf, LocalDate dataDeNascimento, Endereco endereco, TipoCliente tipoCliente) throws CpfInvalidoException, IdadeInvalidaException {

        this.nome = validarNome(nome);
        this.cpf = validarCpf(cpf);
        this.dataDeNascimento = validarDataNascimento(dataDeNascimento);
        this.endereco = endereco;
        this.tipoCliente = tipoCliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
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

    public LocalDate validarDataNascimento(LocalDate dataNascimento) throws IdadeInvalidaException{
        LocalDate diaAtual = LocalDate.now();
        int idade = Period.between(dataNascimento, diaAtual).getYears();
        if (idade < 18){
            throw new IdadeInvalidaException("Você não tem idade o suficiente para ter uma conta!");
        }
        else {
            return dataNascimento;
        }
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "Nome: " + nome + "\n" +
                "CPF: " + cpf + "\n" +
                "Data de Nascimento: " + dataDeNascimento + "\n" +
                "Endereço: " + endereco + "\n" +
                "Sua Categoria de Cliente: " + tipoCliente + "\n" +
                '}';
    }



}
