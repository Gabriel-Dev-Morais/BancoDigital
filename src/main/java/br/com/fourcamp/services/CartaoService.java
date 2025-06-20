package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.*;

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
        cartaoRepository.save(cartao);
        contaRepository.save(cartao.getConta());
        clienteRepository.save(cartao.getConta().getCliente());

    }

    public void pagar(String numero, Double valor, String numeroEAgenciaDestino, String senhaCartao) throws ContaNaoEncontradaException, LimiteAtingidoException, SenhaInvalidaException {
        Cartao cartao = cartaoRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado."));
        Conta contaDestino = contaRepository.findByNumeroEAgencia(numeroEAgenciaDestino)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroEAgenciaDestino));

        Transacao transacao = new Transacao(contaDestino, valor);

            if (senhaCartao.equals(cartao.getSenha())){
                cartao.setTentativas(0);
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

                        cartaoRepository.save(cartao);
                        contaRepository.save(cartao.getConta());
                        contaRepository.save(contaDestino);
                        clienteRepository.save(cartao.getConta().getCliente());
                        clienteRepository.save(contaDestino.getCliente());

                    }
                    else {
                        throw new LimiteAtingidoException("Pagamento não autorizado.");
                    }


                }
            }
            else {
                cartao.setTentativas(cartao.getTentativas() + 1);
                cartaoRepository.save(cartao);
                contaRepository.save(cartao.getConta());
                clienteRepository.save(cartao.getConta().getCliente());
                if (cartao.getTentativas() < 3){
                    throw new SenhaInvalidaException("Senha Incorreta! Tente novamente!");
                }
                else if (cartao.getTentativas() >= 3){
                    cartao.desativarCartao();
                    cartaoRepository.save(cartao);
                    contaRepository.save(cartao.getConta());
                    clienteRepository.save(cartao.getConta().getCliente());
                    throw new LimiteAtingidoException("Cartão bloqueado depois de 3 tentativas!");
                }
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
            List<Transacao> fatura = ((CartaoCredito) cartao).getFatura();
            ((CartaoCredito) cartao).pagarFatura();

            cartaoRepository.save(cartao);
            contaRepository.save(cartao.getConta());
            clienteRepository.save(cartao.getConta().getCliente());

            for(Transacao transacao : fatura) {
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
        try{
            Cartao cartao = cartaoRepository.findByNumero(numero)
                    .orElseThrow(() -> new RuntimeException("Cartão não encontrado!"));

            if (cartao instanceof CartaoCredito){

                ((CartaoCredito) cartao).acionarSeguroFraude(valor);
                cartaoRepository.save(cartao);
                contaRepository.save(cartao.getConta());
                clienteRepository.save(cartao.getConta().getCliente());

            }

        }
        catch (SeguroFraudeInativoException e){
            throw new RuntimeException("Lamentamos, mas você não possui o Seguro Fraude");
        }
    }

}
