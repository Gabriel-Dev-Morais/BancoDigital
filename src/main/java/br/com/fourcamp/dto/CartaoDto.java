package br.com.fourcamp.dto;

import br.com.fourcamp.enums.TipoCartao;

public record CartaoDto(
        String senha,
        TipoCartao tipoCartao
) {
}
