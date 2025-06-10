package br.com.fourcamp.controllers;

import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(name = "/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> salvarConta(@RequestBody Conta conta){
        Conta novaConta = contaService.salvarConta(conta);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    public ResponseEntity<Conta> listarContas(){
        contaService.listarContas();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find/{numeroEAgencia}")
    public ResponseEntity<Conta> pesquisarConta(@PathVariable String numeroEAgencia) throws ContaNaoEncontradaException {
        Optional<Conta> contaEncontrada = contaService.buscarPorNumeroEAgencia(numeroEAgencia);

        if (contaEncontrada.isPresent()){
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{numeroEAgencia}")
    public ResponseEntity<Conta> deletarConta(@PathVariable String numeroEAgencia) throws ContaNaoEncontradaException {
        Optional<Conta> contaEncontrada = contaService.buscarPorNumeroEAgencia(numeroEAgencia);

        if (contaEncontrada.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            contaService.desativarContaPorNumeroEAgencia(numeroEAgencia);
            return ResponseEntity.noContent().build();
        }
    }

}
