package br.com.fourcamp.exceptions;

public class CpfCadastradoException extends Exception{

    public CpfCadastradoException(String cpf) {
        super(cpf + "já está cadastrado!");
    }
}
