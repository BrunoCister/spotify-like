package com.infnet.br.SpotifyLike.request;

import java.io.Serializable;

public class CartaoRequest implements Serializable {

    private Boolean ativo;
    private Double limite;
    private String numero;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
