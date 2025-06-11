package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta salvarConta(Conta conta){
        return contaRepository.save(conta);
    }

    public List<Conta> listarContas(){
        return contaRepository.findAll();
    }

    public Optional<Conta> buscarPorNumeroEAgencia(String numeroEAgencia) throws ContaNaoEncontradaException {
        Optional<Conta> contaEncontrada = contaRepository.findByNumeroEAgencia(numeroEAgencia);

        if (contaEncontrada.isEmpty()) {
            throw new ContaNaoEncontradaException(numeroEAgencia);
        }

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

}
