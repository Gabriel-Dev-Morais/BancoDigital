package br.com.fourcamp.controllers;

import br.com.fourcamp.dto.FraudeDto;
import br.com.fourcamp.dto.SeguroDto;
import br.com.fourcamp.dto.TransacaoDto;
import br.com.fourcamp.exceptions.CartaoNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.LimiteAtingidoException;
import br.com.fourcamp.exceptions.SeguroFraudeInativoException;
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

    @PostMapping("/{numero}/pagar-fatura")
    public ResponseEntity<String> pagarFatura(@PathVariable String numero){
        cartaoService.pagarFatura(numero);
        return ResponseEntity.ok("Fatura paga com sucesso!");
    }

    @PatchMapping("/{numero}/seguros")
    public ResponseEntity<String> ativarDesativarSeguros(@PathVariable String numero, @RequestBody SeguroDto dto){
        cartaoService.ativarDesativarSeguros(numero, dto.seguroFraude(), dto.seguroViagem());
        return ResponseEntity.ok("Seguros modificados!");
    }

    @PostMapping("/{numero}/seguro-fraude")
    public ResponseEntity<String> acionarSeguroFraude(@PathVariable String numero, @RequestBody FraudeDto dto) throws SeguroFraudeInativoException {
        cartaoService.acionarSeguroFraude(numero, dto.valor());
        if (dto.valor() > 5000.0){
            return ResponseEntity.ok("Cobriremos o valor de R$ 5000.00 enquanto você cobrirá R$ "+ (dto.valor() - 5000.0));
        }
        else {
            return ResponseEntity.ok("Cobriremos o valor de R$ "+dto.valor());
        }

    }


}
