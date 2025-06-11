package br.com.fourcamp.controllers;

import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas(){

        return ResponseEntity.ok(contaService.listarContas());
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

}
