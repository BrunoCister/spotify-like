package com.infnet.br.SpotifyLike.repository.transacao;

import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, UUID> {
}
