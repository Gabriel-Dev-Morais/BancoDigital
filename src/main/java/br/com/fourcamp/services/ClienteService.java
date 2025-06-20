package br.com.fourcamp.services;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.CpfInvalidoException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrarCliente(Cliente cliente) throws CpfInvalidoException {

        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new CpfInvalidoException("CPF já cadastrado: " + cliente.getCpf());
        }

    }

    public Cliente autenticar(String cpf, String senha) {
        String cpfLimpo = cpf.replaceAll("[^\\d]", ""); // Remove pontos e traço

        Cliente cliente = clienteRepository.findByCpf(cpfLimpo)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!cliente.getSenhaConta().equals(senha)) {
            throw new RuntimeException("Senha inválida");
        }

        return cliente;
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    public Cliente buscarCliente(String cpf) throws ClienteNaoEncontradoException {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpf));
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

    public void atualizarCliente(Cliente cliente){
         clienteRepository.save(cliente);
    }



}
