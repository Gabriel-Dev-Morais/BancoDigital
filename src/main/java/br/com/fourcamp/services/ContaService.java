package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Conta buscarPorNumeroEAgencia(String numeroEAgencia) throws ContaNaoEncontradaException {
        if (!contaRepository.existsByNumeroEAgencia(numeroEAgencia)){
            throw new ContaNaoEncontradaException(numeroEAgencia);
        }
        else {
            return contaRepository.findByNumeroEAgencia(numeroEAgencia);
        }

    }

    public void desativarContaPorId(String numeroEAgencia) throws ContaNaoEncontradaException {
        if (!contaRepository.existsByNumeroEAgencia(numeroEAgencia)){
            throw new ContaNaoEncontradaException(numeroEAgencia);
        }
        else{
            contaRepository.deleteByNumeroEAgencia(numeroEAgencia);

        }
    }

}
