package br.com.fourcamp.controllers;

import br.com.fourcamp.exceptions.CartaoNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.models.Cartao;
import br.com.fourcamp.services.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(name = "/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping("/{numeroEAgencia}")
    public ResponseEntity<Cartao> cadastrarCartao(@RequestBody Cartao cartao, @PathVariable String numeroEAgencia){
        try{
            Cartao novoCartao = cartaoService.criarCartao(cartao, numeroEAgencia);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCartao);
        }
        catch (ContaNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Cartao> listarCartoes(){
        cartaoService.listarCartoes();
        return ResponseEntity.ok().build();

    }

    @GetMapping("/find/{numero}")
    public ResponseEntity<Cartao> buscarCartao(@PathVariable String numero) throws CartaoNaoEncontradoException {
        Optional<Cartao> cartaoEncontrado = cartaoService.buscarPorNumero(numero);

        if (cartaoEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/{numero}")
    public ResponseEntity<Cartao> deletarCartao(@PathVariable String numero) throws CartaoNaoEncontradoException {
        Optional<Cartao> cartaoEncontrado = cartaoService.buscarPorNumero(numero);

        if (cartaoEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            cartaoService.bloquearCartao(cartaoEncontrado.get());
            return ResponseEntity.noContent().build();
        }
    }

}
