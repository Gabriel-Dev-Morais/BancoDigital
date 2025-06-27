package br.com.fourcamp.controllers;

import br.com.fourcamp.dto.AtualizarClienteDto;
import br.com.fourcamp.dto.AtualizarEnderecoDto;
import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.CpfInvalidoException;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.models.Endereco;
import br.com.fourcamp.services.ClienteService;
import br.com.fourcamp.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
            Cliente clienteEncontrado = clienteService.buscarCliente(cpf);

            return ResponseEntity.ok(clienteEncontrado);
        }
        catch (ClienteNaoEncontradoException e){
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/delete/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf){
        try {
            Cliente cliente = clienteService.buscarCliente(cpf);

            contaService.deletarContaPorNumeroEAgencia(cliente.getConta().getNumeroEAgencia());

            clienteService.deletarCliente(cpf);
            return ResponseEntity.ok().build();
        } catch (ClienteNaoEncontradoException | ContaNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/update/{cpf}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable String cpf,
            @RequestBody AtualizarClienteDto dto) {

        try {
            Cliente cliente = clienteService.buscarCliente(cpf);

            if (dto.nome() != null) cliente.setNome(dto.nome());
            if (dto.senhaConta() != null){

                String senhaCriptografada = passwordEncoder.encode(dto.senhaConta());
                cliente.getConta().setSenha(senhaCriptografada);
                cliente.setSenhaConta(senhaCriptografada);
            }
            if (dto.tipoCliente() != null) cliente.setTipoCliente(dto.tipoCliente());
            if(dto.cpf() != null) cliente.setCpf(dto.cpf());

            clienteService.atualizarCliente(cliente);
            return ResponseEntity.ok("Cliente atualizado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{cpf}/endereco")
    public ResponseEntity<?> atualizarEndereco(@PathVariable String cpf, @RequestBody AtualizarEnderecoDto dto) {
        try {
            Cliente cliente = clienteService.buscarCliente(cpf);

            Endereco endereco = cliente.getEndereco();
            endereco.setNomeRua(dto.nomeRua());
            endereco.setNumero(dto.numero());
            endereco.setCidade(dto.cidade());
            endereco.setBairro(dto.bairro());
            endereco.setEstado(dto.estado());
            endereco.setCep(dto.cep());

            cliente.setEndereco(endereco);
            clienteService.atualizarCliente(cliente);

            return ResponseEntity.ok("Endereço atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
