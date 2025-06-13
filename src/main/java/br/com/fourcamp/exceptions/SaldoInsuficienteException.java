package br.com.fourcamp.exceptions;

public class SaldoInsuficienteException extends Exception{
    public SaldoInsuficienteException(String message) {
        super("Saldo insuficiente!");
    }
}
