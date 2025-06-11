package br.com.fourcamp.controllers;

import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.CpfInvalidoException;
import br.com.fourcamp.models.Cliente;
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
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente){

        try{
            Cliente novoCliente = clienteService.cadastrarCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
        }
        catch (CpfInvalidoException e){
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(){
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/find/{cpf}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String cpf){
        try{
            Optional<Cliente> clienteEncontrado = clienteService.buscarCliente(cpf);

            return clienteEncontrado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
        }
        catch (ClienteNaoEncontradoException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete/{cpf}")
    public ResponseEntity<Cliente> deletarCliente(@PathVariable String cpf){
        try{
            Optional<Cliente> clienteEncontrado = clienteService.buscarCliente(cpf);
            if (clienteEncontrado.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            else {
                clienteService.deletarCliente(cpf);
                return ResponseEntity.ok().build();
            }
        }
        catch (ClienteNaoEncontradoException e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
