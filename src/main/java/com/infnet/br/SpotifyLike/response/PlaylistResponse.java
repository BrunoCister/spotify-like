package com.infnet.br.SpotifyLike.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistResponse {

    public UUID id;
    public String nome;
    //public UUID planoID;
    public List<MusicaResponse> musicas = new ArrayList<>();

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

    /*public UUID getPlanoID() { return planoID; }

    public void setPlanoID(UUID planoID) { this.planoID = planoID; }*/

    public List<MusicaResponse> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<MusicaResponse> musicas) {
        this.musicas = musicas;
    }
}
