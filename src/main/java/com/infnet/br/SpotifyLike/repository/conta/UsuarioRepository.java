package com.infnet.br.SpotifyLike.repository.conta;

import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}
