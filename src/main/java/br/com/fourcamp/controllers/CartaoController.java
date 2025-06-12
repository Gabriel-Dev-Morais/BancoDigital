package br.com.fourcamp.controllers;

import br.com.fourcamp.dto.TransacaoDto;
import br.com.fourcamp.exceptions.CartaoNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.LimiteAtingidoException;
import br.com.fourcamp.models.Cartao;
import br.com.fourcamp.models.Transacao;
import br.com.fourcamp.services.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @GetMapping
    public ResponseEntity<List<Cartao>> listarCartoes(){

        return ResponseEntity.ok(cartaoService.listarCartoes());

    }

    @GetMapping("/find/{numero}")
    public ResponseEntity<Optional<Cartao>> buscarCartao(@PathVariable String numero) throws CartaoNaoEncontradoException {
        Optional<Cartao> cartaoEncontrado = cartaoService.buscarPorNumero(numero);

        if (cartaoEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(cartaoEncontrado);
        }
    }

    @DeleteMapping("/delete/{numero}")
    public ResponseEntity<Cartao> deletarCartao(@PathVariable String numero) throws CartaoNaoEncontradoException {
        Optional<Cartao> cartaoEncontrado = cartaoService.buscarPorNumero(numero);

        if (cartaoEncontrado.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            cartaoService.deletarCartao(numero);
            return ResponseEntity.noContent().build();
        }
    }

    @PatchMapping("/{numero}/desativar")
    public ResponseEntity<String> desativarCartao(@PathVariable String numero) throws CartaoNaoEncontradoException {
        cartaoService.desativarCartao(numero);
        return ResponseEntity.ok("Cartão desativado!");
    }

    @PostMapping("/{numero}/pagar")
    public ResponseEntity<String> pagar(@PathVariable String numero, @RequestBody TransacaoDto dto) throws ContaNaoEncontradaException, LimiteAtingidoException {
        cartaoService.pagar(numero, dto.valor(), dto.numeroEAgenciaDestino());
        return ResponseEntity.ok("Pagamento bem-sucedido!");
    }

    @GetMapping("/{numero}/fatura")
    public ResponseEntity<List<Transacao>> verFatura(@PathVariable String numero){

        return ResponseEntity.ok(cartaoService.mostrarFatura(numero));
    }

}
