package br.com.casadocodigo.boaviagem.domain;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by p001234 on 27/04/15.
 */
public class Gasto {

    public static final String TABELA = "gasto";
    public static final String _ID = "_id";
    public static final String VIAGEM_ID = "viagem_id";
    public static final String CATEGORIA = "categoria";
    public static final String DATA = "data";
    public static final String DESCRICAO = "descricao";
    public static final String VALOR = "valor";
    public static final String LOCAL = "local";

    private Long id;
    private Date data;
    private String categoria;
    private String descricao;
    private Double valor;
    private String local;

    private Long viagemId;

    public Gasto() {}

    public Gasto(Long id, Date data, String categoria, String descricao, Double valor, String local,
                 Long viagemId) {
        this.id = id;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.local = local;
        this.viagemId = viagemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Long getViagemId() {
        return viagemId;
    }

    public void setViagemId(Long viagemId) {
        this.viagemId = viagemId;
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put(Gasto._ID, this.id);
        values.put(Gasto.VIAGEM_ID, this.viagemId);
        values.put(Gasto.CATEGORIA, this.categoria);
        values.put(Gasto.VALOR, this.valor);
        values.put(Gasto.DATA, this.data.getTime());
        values.put(Gasto.DESCRICAO, this.descricao);
        values.put(Gasto.LOCAL, this.local);

        return values;
    }
}
