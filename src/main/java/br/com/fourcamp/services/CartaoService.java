package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.CartaoNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Cartao;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.repositories.CartaoRepository;
import br.com.fourcamp.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ContaRepository contaRepository;

    public Cartao criarCartao(Cartao cartao, String numeroEAgencia) throws ContaNaoEncontradaException {
        Optional<Conta> contaEncontrada = contaRepository.findByNumeroEAgencia(numeroEAgencia);

        if (contaEncontrada.isEmpty()){
            throw new ContaNaoEncontradaException(numeroEAgencia);
        }

        cartao.setConta(contaEncontrada.get());
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> listarCartoes(){
        return cartaoRepository.findAll();
    }

    public Optional<Cartao> buscarPorNumero(String numero) throws CartaoNaoEncontradoException {
        if (!cartaoRepository.existsByNumero(numero)){
            throw new CartaoNaoEncontradoException(numero);
        }
        else {
            return cartaoRepository.findByNumero(numero);
        }
    }

    @Transactional
    public void deletarCartao(String numero){
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));
        cartaoRepository.deleteByNumero(cartao.getNumero());

    }

    @Transactional
    public void desativarCartao(String numero){

        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado."));
        cartao.setAtivo(false);
    }


}
