package com.infnet.br.SpotifyLike.application.conta;

import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.exceptions.*;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import com.infnet.br.SpotifyLike.repository.conta.UsuarioRepository;
import com.infnet.br.SpotifyLike.repository.streaming.MusicaRepository;
import com.infnet.br.SpotifyLike.repository.transacao.PlanoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PlanoRepository planoRepository;
    @Autowired
    MusicaRepository musicaRepository;
    @Autowired
    EntityManager entityManager;

    @Transactional
    public Usuario criarConta(String nome, UUID planoId, Cartao cartao) throws Exception {

        Plano plano = this.planoRepository.getReferenceById(planoId);

        if (plano == null) {
            throw new PlanoNotFoundException(ExceptionMessages.PLANO_NOT_FOUND);
        }

        Usuario usuario = new Usuario();
        usuario.criarUsuario(nome, plano, cartao);
        cartao.setUsuario(usuario);
        usuario.getCartoes().add(cartao);

        if (!entityManager.contains(usuario)) {
            usuario = entityManager.merge(usuario);
        }

        this.usuarioRepository.save(usuario);

        return usuario;

    }

    public Usuario obterUsuario(UUID id) {

        return this.usuarioRepository.findById(id).get();
    }

    public void favoritarMusica(UUID idUsuario, UUID idMusica) throws UsuarioNotFoundException, PlaylistNotFoundException, MusicaNotFoundException, IOException, URISyntaxException, InterruptedException {

        Usuario usuario = this.usuarioRepository.getReferenceById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException(ExceptionMessages.USUARIO_NOT_FOUND);
        }

        Musica musica = verificarMusica(idMusica);


        usuario.favoritarMusica(musica);
        this.usuarioRepository.save(usuario);
    }

    public void desfavoritarMusica(UUID idUsuario, UUID idMusica) throws UsuarioNotFoundException, MusicaNotFoundException, IOException, URISyntaxException, InterruptedException, PlaylistNotFoundException {

        Usuario usuario = this.usuarioRepository.getReferenceById(idUsuario);
        if (usuario == null) {
            throw new UsuarioNotFoundException(ExceptionMessages.USUARIO_NOT_FOUND);
        }

        Musica musica = verificarMusica(idMusica);

        usuario.desfavoritarMusica(musica);
        this.usuarioRepository.save(usuario);
    }

    public Musica verificarMusica(UUID idMusica) throws MusicaNotFoundException, IOException, URISyntaxException, InterruptedException {

        Musica musica = this.musicaRepository.getMusica(idMusica);
        if (musica == null) {
            throw new MusicaNotFoundException(ExceptionMessages.MUSICA_NOT_FOUND);
        }
        return musica;
    }
}
