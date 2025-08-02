package mvrpl.dev.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value="pessoas")
public class User {

    @PrimaryKey(value="id")
    @Column("id") private String id;
    @Column("criado_em") private java.time.LocalDate criadoEm;
    @Column("email") private String email;
    @Column("nome") private String nome;

    public java.time.LocalDate getCriadoEm() {
        return criadoEm;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }
}