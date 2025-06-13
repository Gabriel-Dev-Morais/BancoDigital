package br.com.fourcamp.dto;

public record PagarComCartaoDto(
        Double valor,
        String numeroEAgenciaDestino,
        String nomeContaDestino,
        String senhaCartao
) {
}
