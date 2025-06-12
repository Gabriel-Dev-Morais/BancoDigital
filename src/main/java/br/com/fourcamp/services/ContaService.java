package br.com.fourcamp.services;

import br.com.fourcamp.enums.TipoCartao;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Cartao;
import br.com.fourcamp.models.CartaoCredito;
import br.com.fourcamp.models.CartaoDebito;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.repositories.CartaoRepository;
import br.com.fourcamp.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Conta buscarPorNumeroEAgencia(String numeroEAgencia) throws ContaNaoEncontradaException {
        Conta contaEncontrada = contaRepository.findByNumeroEAgencia(numeroEAgencia)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada!"));


        return contaEncontrada;
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

}
