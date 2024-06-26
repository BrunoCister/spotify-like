package com.infnet.br.SpotifyLike;

import com.infnet.br.SpotifyLike.application.conta.UsuarioService;
import com.infnet.br.SpotifyLike.domain.conta.Playlist;
import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.exceptions.*;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import com.infnet.br.SpotifyLike.domain.transacao.Assinatura;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.request.UsuarioRequest;
import com.infnet.br.SpotifyLike.response.MusicaResponse;
import com.infnet.br.SpotifyLike.response.PlaylistResponse;
import com.infnet.br.SpotifyLike.response.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@RequestBody UsuarioRequest usuarioRequest) throws Exception {

        Cartao cartao = new Cartao();
        cartao.setLimite(usuarioRequest.getCartao().getLimite());
        cartao.setAtivo(usuarioRequest.getCartao().getAtivo());
        cartao.setNumero(usuarioRequest.getCartao().getNumero());

        Usuario usuarioCriado = null;
        try {
            usuarioCriado = this.usuarioService.criarConta(usuarioRequest.getNome(), usuarioRequest.getPlanoId(), cartao);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UsuarioResponse usuarioResponse = usuarioParaResponse(usuarioCriado);

        if (usuarioRequest != null) {
            return new ResponseEntity<>(usuarioResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<UsuarioResponse> obter(@PathVariable UUID id) throws AssinaturaNotActiveException {

        Usuario usuario = this.usuarioService.obterUsuario(id);

        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var response = usuarioParaResponse(usuario);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("{id}/favoritar/{idMusica}")
    public ResponseEntity<UsuarioResponse> favoritarMusica(@PathVariable UUID id,@PathVariable UUID idMusica) throws MusicaNotFoundException, PlaylistNotFoundException, IOException, URISyntaxException, InterruptedException, UsuarioNotFoundException, AssinaturaNotActiveException {

        try {
            this.usuarioService.favoritarMusica(id, idMusica);

            Usuario usuario = this.usuarioService.obterUsuario(id);
            UsuarioResponse usuarioResponse = usuarioParaResponse(usuario);

            return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("{id}/desfavoritar/{idMusica}")
    public ResponseEntity<UsuarioResponse> desfavoritarMusica(@PathVariable UUID id,@PathVariable UUID idMusica) throws MusicaNotFoundException, PlaylistNotFoundException, IOException, URISyntaxException, InterruptedException, UsuarioNotFoundException, AssinaturaNotActiveException {

        this.usuarioService.desfavoritarMusica(id, idMusica);

        Usuario usuario = this.usuarioService.obterUsuario(id);
        UsuarioResponse usuarioResponse = usuarioParaResponse(usuario);

        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

    private UsuarioResponse usuarioParaResponse(Usuario usuarioCriado) throws AssinaturaNotActiveException {

        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuarioCriado.getId());
        response.setNome(usuarioCriado.getNome());
        Assinatura assinaturaAtiva = usuarioCriado.getAssinaturas().stream().filter(Assinatura::getAtivo).findFirst().orElse(null);
        if (assinaturaAtiva != null) {
            response.setPlanoId(assinaturaAtiva.getPlano().getId());
        } else {
            throw new AssinaturaNotActiveException(ExceptionMessages.ASSINATURA_NOT_ACTIVE);
        }

        for (Playlist playlist : usuarioCriado.getPlaylists()) {
            PlaylistResponse playlistResponse = new PlaylistResponse();
            playlistResponse.setId(playlist.getId());
            playlistResponse.setNome(playlist.getNome());
            response.getPlaylists().add(playlistResponse);

            for (Musica musica : playlist.getMusicas()) {
                MusicaResponse musicaResponse = new MusicaResponse();
                musicaResponse.setId(musica.getId());
                musicaResponse.setNome(musica.getNome());
                musicaResponse.setDuracao(musica.getDuracao());
                playlistResponse.getMusicas().add(musicaResponse);
            }
        }
        return response;
    }
}
