package mvrpl.dev.model;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Pessoas extends CassandraRepository<User, String> {
    @Query("SELECT id, criado_em, email, nome FROM pessoas WHERE email = ?0")
    Optional<User> findByEmail(final String email);

    @Query("SELECT id, criado_em, email, nome FROM pessoas")
    List<User> findAll();
}