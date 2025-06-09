package br.com.fourcamp.exceptions;

public class ClienteNaoEncontradoException extends Exception{
    public ClienteNaoEncontradoException(String nome) {
        super("Conta com ID "+nome+" não encontrada!");
    }
}
