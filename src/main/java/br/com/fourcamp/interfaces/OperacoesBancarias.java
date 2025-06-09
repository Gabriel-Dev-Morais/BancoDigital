package br.com.fourcamp.interfaces;

import br.com.fourcamp.models.Conta;

public interface OperacoesBancarias {

    void exibirSaldo();

    void sacar(Double valor);

    void depositar(Double valor);

    void transferirPix(Double valor, Conta conta);

}
