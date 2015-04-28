package br.com.casadocodigo.boaviagem.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.casadocodigo.boaviagem.DatabaseHelper;
import br.com.casadocodigo.boaviagem.domain.Gasto;

/**
 * Created by p001234 on 28/04/15.
 */
public class GastoDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public GastoDAO(Context context) {
        this.helper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDb() {
        if (this.db == null) {
            this.db = helper.getWritableDatabase();
        }
        return this.db;
    }

    public List<Gasto> listarGasto(String idViagem) {

        Cursor cursor = getDb().query(
                DatabaseHelper.Gasto.TABELA,
                DatabaseHelper.Gasto.COLUNAS,
                "viagem_id = ?", new String[] {idViagem}, null, null, null);

        List<Gasto> gastos = new ArrayList<>();
        while (cursor.moveToNext()) {
            gastos.add(criarGasto(cursor));
        }

        return gastos;
    }

    private Gasto criarGasto(Cursor cursor) {

        Gasto gasto = new Gasto();
        gasto.setId(cursor.getLong(0));
        gasto.setViagemId(cursor.getLong(1));
        gasto.setCategoria(cursor.getString(2));
        gasto.setData(new Date(cursor.getLong(3)));
        gasto.setDescricao(cursor.getString(4));
        gasto.setValor(cursor.getDouble(5));
        gasto.setLocal(cursor.getString(6));

        return gasto;
    }

    public long saveOrUpdate(Gasto gasto) {

        if (gasto.getId() == null) {
            return getDb().insert(Gasto.TABELA, null, gasto.getContentValues());
        }

        return getDb().update(Gasto.TABELA, gasto.getContentValues(),
                "_id = ?", new String[] {gasto.getId().toString()});
    }

    public void close() {
        helper.close();
    }
}
