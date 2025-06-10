package br.com.fourcamp.controllers;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(name = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente){

            Cliente novoCliente = clienteService.cadastrarCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);

    }

    @GetMapping
    public ResponseEntity<Cliente> listarClientes(){
        clienteService.listarClientes();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{nome}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String nome) throws ClienteNaoEncontradoException {
        Optional<Cliente> clienteEncontrado = clienteService.buscarPorNome(nome);

        if (clienteEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/delete/{nome}")
    public ResponseEntity<Cliente> deletarCliente(String nome) throws ClienteNaoEncontradoException {
        Optional<Cliente> clienteEncontrado = clienteService.buscarPorNome(nome);
        if (clienteEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            clienteService.deletarCliente(nome);
            return ResponseEntity.noContent().build();
        }
    }

}
