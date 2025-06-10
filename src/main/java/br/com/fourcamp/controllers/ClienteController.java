package br.com.fourcamp.controllers;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.CpfCadastradoException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.models.ContaCorrente;
import br.com.fourcamp.services.ClienteService;
import br.com.fourcamp.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) throws CpfCadastradoException {

        Cliente novoCliente = clienteService.cadastrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);

    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(){
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/find/{cpf}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String cpf) throws ClienteNaoEncontradoException {
        Optional<Cliente> clienteEncontrado = clienteService.buscarCliente(cpf);

        if (clienteEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(clienteEncontrado.get());
        }
    }

    @DeleteMapping("/delete/{cpf}")
    public ResponseEntity<Cliente> deletarCliente(@PathVariable String cpf) throws ClienteNaoEncontradoException {
        Optional<Cliente> clienteEncontrado = clienteService.buscarCliente(cpf);
        if (clienteEncontrado.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else {
            clienteService.deletarCliente(cpf);
            return ResponseEntity.ok().build();
        }
    }

}
