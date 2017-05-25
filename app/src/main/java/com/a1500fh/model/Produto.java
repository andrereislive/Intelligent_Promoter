package com.a1500fh.model;

import java.util.List;

/**
 * Created by Andre on 25/05/2017.
 */

public class Produto {
    public static final String PRODUTO_ENTIDADE = "PRODUTO";
    public static final String PRODUTO_NOME = "PRODUTO_NOME";
    public static final String PRODUTO_ID = "ID";

    private Integer id;
    private String nome;


    public Produto() {

    }

    public Produto(Integer id) {
        this.id = id;
    }

    public Produto(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String[] asStringArray(List<Produto> proList) {
        String[] array = new String[proList.size()];
        int x = 0;
        for (Produto ite : proList) {
            array[x] = ite.getNome();
            x++;
        }

        return array;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
