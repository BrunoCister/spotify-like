package com.infnet.br.SpotifyLike.request;

import java.io.Serializable;
import java.util.UUID;

public class UsuarioRequest implements Serializable {

    private UUID planoId;
    private String nome;
    private CartaoRequest cartao;

    public UUID getPlanoId() {
        return planoId;
    }

    public void setPlanoId(UUID planoId) {
        this.planoId = planoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CartaoRequest getCartao() {
        return cartao;
    }

    public void setCartao(CartaoRequest cartao) {
        this.cartao = cartao;
    }
}
