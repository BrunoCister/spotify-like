package com.infnet.br.SpotifyLike.domain.exceptions;

public class DoubleTransactionException extends Exception {
    public DoubleTransactionException(String message) {
        super(message);
    }
}
