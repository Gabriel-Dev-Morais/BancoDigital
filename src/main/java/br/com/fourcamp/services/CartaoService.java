package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.CartaoNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.LimiteAtingidoException;
import br.com.fourcamp.exceptions.SeguroFraudeInativoException;

import br.com.fourcamp.models.*;
import br.com.fourcamp.repositories.CartaoRepository;
import br.com.fourcamp.repositories.ClienteRepository;
import br.com.fourcamp.repositories.ContaRepository;
import br.com.fourcamp.repositories.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public CartaoService(CartaoRepository cartaoRepository, ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public Cartao criarCartao(Cartao cartao, String numeroEAgencia) throws ContaNaoEncontradaException {
        Optional<Conta> contaEncontrada = contaRepository.findByNumeroEAgencia(numeroEAgencia);

        if (contaEncontrada.isEmpty()){
            throw new ContaNaoEncontradaException(numeroEAgencia);
        }

        cartao.setConta(contaEncontrada.get());
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> listarCartoes(){
        return cartaoRepository.findByAtivoTrue();
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
        cartaoRepository.deleteByNumero(numero);
    }

    public void pagar(String numero, Double valor, String numeroEAgenciaDestino) throws ContaNaoEncontradaException, LimiteAtingidoException {
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado."));
        Conta contaDestino = contaRepository.findByNumeroEAgencia(numeroEAgenciaDestino)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgenciaDestino));

        Transacao transacao = new Transacao(contaDestino, valor);

        if (cartao instanceof CartaoDebito){
            ((CartaoDebito) cartao).pagar(transacao);
            contaRepository.save(cartao.getConta());
            contaRepository.save(contaDestino);

        }

        else if (cartao instanceof CartaoCredito){
            boolean autorizado = ((CartaoCredito) cartao).permitirPagamento(transacao);

            if (autorizado){
                transacao.setCartaoCredito((CartaoCredito) cartao);
                transacaoRepository.save(transacao);
            }
            contaRepository.save(cartao.getConta());
            contaRepository.save(contaDestino);
            clienteRepository.save(cartao.getConta().getCliente());
            clienteRepository.save(contaDestino.getCliente());
        }

        else if (cartao instanceof CartaoCredito){
            boolean autorizado = ((CartaoCredito) cartao).permitirPagamento(transacao);

            if (autorizado){
                transacao.setCartaoCredito((CartaoCredito) cartao);
                transacaoRepository.save(transacao);
            }
            contaRepository.save(cartao.getConta());
            contaRepository.save(contaDestino);
        }


    }

    public List<Transacao> mostrarFatura(String numero){
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado."));

         if (cartao instanceof CartaoCredito){
             return transacaoRepository.findByCartaoCreditoNumero(numero);
        }
        return Collections.emptyList();
    }

    public void pagarFatura(String numero){
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado."));

        if (cartao instanceof CartaoCredito){
            CartaoCredito cred = (CartaoCredito) cartao;
            List<Transacao> fatura = cred.getFatura();
            cred.pagarFatura();

            cartaoRepository.save(cartao);
            contaRepository.save(cartao.getConta());
            clienteRepository.save(cartao.getConta().getCliente());

            for(Transacao transacao : fatura){
                contaRepository.save(transacao.getContaDestino());
                clienteRepository.save(transacao.getContaDestino().getCliente());
            }

            transacaoRepository.deleteAll(fatura);
        }
    }

    public void ativarDesativarSeguros(String numero, Boolean seguroFraude, Boolean seguroViagem) {
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

        if (cartao instanceof CartaoCredito){
            ((CartaoCredito) cartao).setSeguroFraude(seguroFraude);
            ((CartaoCredito) cartao).setSeguroViagem(seguroViagem);
            ((CartaoCredito) cartao).acionarSeguroViagem();
            cartaoRepository.save(cartao);
            contaRepository.save(cartao.getConta());
            clienteRepository.save(cartao.getConta().getCliente());
        }
    }

    public void acionarSeguroFraude(String numero, Double valor) throws SeguroFraudeInativoException {
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

        if (cartao instanceof CartaoCredito){

                ((CartaoCredito) cartao).acionarSeguroFraude(valor);
                cartaoRepository.save(cartao);
                contaRepository.save(cartao.getConta());
            clienteRepository.save(cartao.getConta().getCliente());

        }
    }

}
