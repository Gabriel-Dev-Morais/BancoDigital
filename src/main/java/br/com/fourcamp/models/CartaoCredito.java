package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCliente;
import br.com.fourcamp.exceptions.SenhaInvalidaException;
import br.com.fourcamp.interfaces.Seguro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
public class CartaoCredito extends Cartao implements Seguro {

    @OneToMany(mappedBy = "cartaoCredito")
    private List<Transacao> fatura;

    @Column(name = "limite")
    private Double limite;

    @Column(name = "total_da_fatura")
    private Double totalFatura;

    @Column(name = "status_seguro_viagem")
    private Boolean seguroViagem;

    @Column(name = "status_seguro_fraude")
    private Boolean seguroFraude;

    public CartaoCredito(Long id, Conta conta,String senha, Boolean seguroViagem, Boolean seguroFraude){
        super(id, conta, senha);
        this.fatura = new ArrayList<>();
        this.totalFatura = 0.0;
        this.seguroViagem = seguroViagem;
        acionarSeguroViagem();
        this.seguroFraude = seguroFraude;
    }

    public List<Transacao> getFatura() {
        return fatura;
    }

    public void setFatura(List<Transacao> fatura) {
        this.fatura = fatura;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getTotalFatura() {
        return totalFatura;
    }

    public void setTotalFatura(Double totalFatura) {
        this.totalFatura = totalFatura;
    }

    public Boolean getSeguroViagem() {
        return seguroViagem;
    }

    public void setSeguroViagem(Boolean seguroViagem) {
        this.seguroViagem = seguroViagem;
    }

    public Boolean getSeguroFraude() {
        return seguroFraude;
    }

    public void setSeguroFraude(Boolean seguroFraude) {
        this.seguroFraude = seguroFraude;
    }

    public void adicionarFatura(Transacao transacao){
        this.getFatura().add(transacao);
        this.setTotalFatura(this.totalFatura += transacao.getValor());
    }



    public void definirLimite(){
        if(this.getConta().getCliente().getTipoCliente() == TipoCliente.COMUM){
            this.setLimite(1000.0);
        }
        else if (this.getConta().getCliente().getTipoCliente() == TipoCliente.SUPER){
            this.setLimite(5000.0);
        }
        else {
            this.setLimite(10000.0);
        }

    }

    public void pagarFatura(){

        Double valorFatura = this.getTotalFatura();

        if (valorFatura >= (0.8 * this.getLimite())){
            this.setTotalFatura(valorFatura += (0.05 * valorFatura));
            System.out.println(valorFatura);
            this.getConta().setSaldo(this.getConta().getSaldo() - this.getTotalFatura());
            System.out.println("Saldo atual: "+this.getConta().getSaldo());
            this.getFatura().clear();
            this.setTotalFatura(0.0);
        }
        else {
            this.getConta().setSaldo(this.getConta().getSaldo() - valorFatura);
            System.out.println("Saldo atual: "+this.getConta().getSaldo());
            this.getFatura().clear();
            this.setTotalFatura(0.0);
        }

    }

    public Boolean permitirPagamento(Transacao transacao){
        if ((this.getTotalFatura() < this.getLimite()) && this.getAtivo()){
            if (transacao.getValor() <= (this.getLimite() - this.getTotalFatura())){
                adicionarFatura(transacao);
                transacao.getContaDestino().setSaldo(transacao.getContaDestino().getSaldo() - transacao.getValor());
                return true;
            }
            else {
                System.out.println("Limite do cartão atingido ou cartão desativado!");
                return false;
            }
        }
        return false;
    }



    @Override
    public void acionarSeguroViagem() {
        if ((this.getSeguroViagem() && (this.getConta().getCliente().getTipoCliente() == TipoCliente.SUPER || this.getConta().getCliente().getTipoCliente() == TipoCliente.COMUM)) && this.getAtivo()){
            this.setTotalFatura(this.getTotalFatura() + 50.0);
        }
    }

    @Override
    public void acionarSeguroFraude(Double valorFraude) {
        if ((this.getSeguroFraude() && valorFraude <= 5000.0) && this.getAtivo()){
            System.out.println("Cobriremos o valor de R$"+valorFraude);
            this.getConta().setSaldo(this.getConta().getSaldo() + valorFraude);
        }
        else {
            if (this.getSeguroFraude() && valorFraude > 5000.0){
                System.out.println("Cobriremos o valor de R$ 5000.00\nVocê cobrirá o restante (R$ "+(valorFraude - 5000)+")");
                this.getConta().setSaldo(this.getConta().getSaldo() + (5000 - (valorFraude - 5000)));
            }
            else {
                System.out.println("Lamentamos, mas você não possui o Seguro de Fraude.");
            }
        }
    }

    @Override
    public void ativarDesativarSeguroViagem() {
        this.setSeguroViagem(!this.getSeguroViagem());
    }

    @Override
    public void ativarDesativarSeguroFraude() {
        this.setSeguroFraude(!this.getSeguroFraude());
    }

}
