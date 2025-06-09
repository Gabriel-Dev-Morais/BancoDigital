package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.CartaoNaoEncontradoException;
import br.com.fourcamp.models.Cartao;
import br.com.fourcamp.repositories.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    public Cartao criarCartao(Cartao cartao){
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> listarCartoes(){
        return cartaoRepository.findAll();
    }

    public Cartao buscarPorNumero(String numero) throws CartaoNaoEncontradoException {
        if (!cartaoRepository.existsByNumero(numero)){
            throw new CartaoNaoEncontradoException(numero);
        }
        else {
            return cartaoRepository.findByNumero(numero);
        }
    }

    public void bloquearCartao(Cartao cartao) throws CartaoNaoEncontradoException {
        if (cartaoRepository.existsByNumero(cartao.getNumero())){
            if (cartao.getAtivo()){
                cartao.setAtivo(false);
                cartaoRepository.save(cartao);
            }
        }
        else {
            throw new CartaoNaoEncontradoException(cartao.getNumero());
        }
    }
}
