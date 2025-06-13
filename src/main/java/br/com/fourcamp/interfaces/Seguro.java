package br.com.fourcamp.interfaces;

import br.com.fourcamp.exceptions.SeguroFraudeInativoException;

public interface Seguro {

    void acionarSeguroViagem();
    void acionarSeguroFraude(Double valorFraude) throws SeguroFraudeInativoException;

    void ativarDesativarSeguroViagem();
    void ativarDesativarSeguroFraude();


}
