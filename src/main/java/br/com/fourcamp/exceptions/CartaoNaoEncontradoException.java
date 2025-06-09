package br.com.fourcamp.exceptions;

public class CartaoNaoEncontradoException extends Exception{
    public CartaoNaoEncontradoException(String numero) {
        super("Conta com número "+numero+" não encontrada!");
    }
}
