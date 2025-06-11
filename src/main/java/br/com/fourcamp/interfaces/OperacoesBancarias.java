package br.com.fourcamp.interfaces;

import br.com.fourcamp.exceptions.SaldoInsuficienteException;
import br.com.fourcamp.models.Conta;

public interface OperacoesBancarias {

    void exibirSaldo();

    void sacar(Double valor) throws SaldoInsuficienteException;

    void depositar(Double valor);

    void transferirPix(Double valor, Conta conta);

}
