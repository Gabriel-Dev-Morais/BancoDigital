package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.CpfCadastradoException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public Cliente cadastrarCliente(Cliente cliente) throws CpfCadastradoException {

        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new CpfCadastradoException("CPF já cadastrado: " + cliente.getCpf());
        }

    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarCliente(String cpf) throws ClienteNaoEncontradoException {
        Optional<Cliente> clienteEncontrado = clienteRepository.findByCpf(cpf);

        if (clienteEncontrado.isEmpty()) {
            throw new ClienteNaoEncontradoException(cpf);
        }

        return clienteEncontrado;
    }

    @Transactional
    public void deletarCliente(String cpf) throws ClienteNaoEncontradoException {
        if (!clienteRepository.existsByCpf(cpf)){
            throw new ClienteNaoEncontradoException(cpf);
        }
        else {
            clienteRepository.deleteByCpf(cpf);
        }
    }



}
