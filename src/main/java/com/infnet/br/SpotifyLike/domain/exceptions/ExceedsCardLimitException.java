package com.infnet.br.SpotifyLike.domain.exceptions;

public class ExceedsCardLimitException extends  Exception {
    public ExceedsCardLimitException (String message) {
        super(message);
    }
}
