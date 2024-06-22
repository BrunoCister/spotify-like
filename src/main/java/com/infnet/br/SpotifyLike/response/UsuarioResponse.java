package com.infnet.br.SpotifyLike.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioResponse {

    public UUID id;
    public String nome;
    public UUID planoId;
    public List<PlaylistResponse> playlists = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UUID getPlanoId() {
        return planoId;
    }

    public void setPlanoId(UUID planoId) {
        this.planoId = planoId;
    }

    public List<PlaylistResponse> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistResponse> playlists) {
        this.playlists = playlists;
    }
}
