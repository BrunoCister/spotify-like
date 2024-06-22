package com.infnet.br.SpotifyLike.repository.streaming;

import com.infnet.br.SpotifyLike.domain.exceptions.ExceptionMessages;
import com.infnet.br.SpotifyLike.domain.exceptions.MusicaNotFoundException;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.Retry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@Repository
public class MusicaRepository {

    private final RestTemplate restTemplate;
    private final Retry retry;

    public MusicaRepository(RestTemplate restTemplate, Retry retry) {
        this.restTemplate = restTemplate;
        this.retry = retry;
    }

     public Musica getMusica(UUID id) throws IOException, InterruptedException, URISyntaxException, MusicaNotFoundException {

         String baseUrl = "http://localhost:8081/musica/" + id;

         return Retry.decorateSupplier(retry, () -> {
             Musica musica = restTemplate.getForObject(baseUrl, Musica.class);
             if (musica == null) {
                 throw new RuntimeException("Musica nao encontrada.");
             }
             return musica;
         }).get();
    };

}
