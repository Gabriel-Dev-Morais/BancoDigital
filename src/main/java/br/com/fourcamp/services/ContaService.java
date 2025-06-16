package br.com.fourcamp.services;

import br.com.fourcamp.enums.TipoCartao;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.SaldoInsuficienteException;
import br.com.fourcamp.models.*;
import br.com.fourcamp.repositories.CartaoRepository;
import br.com.fourcamp.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;



    public Conta salvarConta(Conta conta){
        return contaRepository.save(conta);
    }

    public List<Conta> listarContas(){
        return contaRepository.findAll();
    }

    public Conta buscarPorNumeroEAgencia(String numeroEAgencia){

        return contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
    }

    @Transactional
    public void deletarContaPorNumeroEAgencia(String numeroEAgencia) throws ContaNaoEncontradaException {
        if (!contaRepository.existsByNumeroEAgencia(numeroEAgencia)){
            throw new ContaNaoEncontradaException(numeroEAgencia);
        }
        else{
            contaRepository.deleteByNumeroEAgencia(numeroEAgencia);
        }
    }

    public Cartao criarCartao(String numeroEAgencia, String senha, TipoCartao tipo) throws ContaNaoEncontradaException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));

        Cartao cartao;
        if (tipo == TipoCartao.CREDITO) {
            CartaoCredito cred = new CartaoCredito(null, conta, senha, tipo, false, false);
            cred.definirLimite();
            cartao = cred;
        } else {
            CartaoDebito deb = new CartaoDebito(null, conta, senha, tipo);
            deb.setLimite(null);
            cartao = deb;
        }

        cartao = cartaoRepository.save(cartao);
        conta.cadastrarCartao(cartao);
        contaRepository.save(conta);

        return cartao;
    }


    public List<Cartao> listarCartoesAtivos(String cpf){
        return cartaoRepository.findByContaClienteCpfAndAtivoTrue(cpf);
    }

    public void depositar(String numeroEAgencia, Double valor) throws ContaNaoEncontradaException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));

        conta.depositar(valor);
        contaRepository.save(conta);
    }

    public void sacar(String numeroEAgencia, Double valor) throws ContaNaoEncontradaException, SaldoInsuficienteException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));

        conta.sacar(valor);
        contaRepository.save(conta);
    }

    public void transferir(String numeroEAgencia, Double valor, String numeroEAgenciaDestino) throws ContaNaoEncontradaException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));
        Conta contaDestino = contaRepository.findByNumeroEAgencia(numeroEAgenciaDestino)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgenciaDestino));

        conta.transferirPix(valor, contaDestino);
        contaRepository.save(conta);
        contaRepository.save(contaDestino);

    }

    public void taxaRendimento(String numeroEAgencia) throws ContaNaoEncontradaException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));

        if (conta instanceof ContaPoupanca){
            ((ContaPoupanca) conta).taxaRendimento();
            contaRepository.save(conta);
        }
    }

    public void taxaManutencao(String numeroEAgencia) throws ContaNaoEncontradaException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));

        if (conta instanceof ContaCorrente){
            ((ContaCorrente) conta).taxaManutencao();
            contaRepository.save(conta);
        }
    }

    public Double exibirSaldo(String numeroEAgencia) throws ContaNaoEncontradaException {
        Conta conta = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgencia));

        return conta.getSaldo();
    }

}
