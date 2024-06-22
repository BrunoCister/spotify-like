package com.infnet.br.SpotifyLike.domain.exceptions;

public class PlaylistNotFoundException extends Exception {
    public PlaylistNotFoundException(String message) {
        super(message);
    }
}
