package br.com.fourcamp.dto;

public record TransacaoDto(
        Double valor,
        String numeroEAgenciaDestino
) {
}
