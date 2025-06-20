package br.com.fourcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Login(String cpf, @JsonProperty("senha")String password) {}

