package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public Cliente cadastrarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorNome(String nome) throws ClienteNaoEncontradoException {
        Optional<Cliente> clienteEncontrado = clienteRepository.findByNome(nome);

        if (clienteEncontrado.isEmpty()) {
            throw new ClienteNaoEncontradoException(nome);
        }

        return clienteEncontrado;
    }

    public void deletarCliente(String nome) throws ClienteNaoEncontradoException {
        if (!clienteRepository.existsByNome(nome)){
            throw new ClienteNaoEncontradoException(nome);
        }
        else {
            clienteRepository.deleteByNome(nome);
        }
    }

}
