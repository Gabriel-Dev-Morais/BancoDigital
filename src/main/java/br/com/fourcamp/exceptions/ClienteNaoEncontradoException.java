package br.com.fourcamp.exceptions;

public class ClienteNaoEncontradoException extends Exception{
    public ClienteNaoEncontradoException(String cpf) {
        super("Conta com CPF "+cpf+" não encontrada!");
    }
}
