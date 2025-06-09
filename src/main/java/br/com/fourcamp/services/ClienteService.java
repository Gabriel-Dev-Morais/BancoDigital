package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Cliente buscarPorNome(String nome) throws ClienteNaoEncontradoException {
        if (!clienteRepository.existsByNome(nome)){
            throw new ClienteNaoEncontradoException(nome);
        }
        else {
            return clienteRepository.findByNome(nome);
        }

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
