package br.com.fiap.batchaluno.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "alunos")
public class Aluno {

    @Id
    private String id;

    private String nome;
    private String rm;
    private String num_cartao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getNum_cartao() {
        return num_cartao;
    }

    public void setNum_cartao(String num_cartao) {
        this.num_cartao = num_cartao;
    }
}
