package br.com.fourcamp.exceptions;

public class ContaNaoEncontradaException extends Exception{

    public ContaNaoEncontradaException(String numeroEAgencia){
        super("Conta com ID "+numeroEAgencia+" não encontrada!");
    }
}
