package br.com.casadocodigo.boaviagem.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.casadocodigo.boaviagem.DatabaseHelper;
import br.com.casadocodigo.boaviagem.domain.Viagem;

/**
 * Created by p001234 on 27/04/15.
 */
public class BoaViagemDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public BoaViagemDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getDb() {
        if (this.db == null) {
            this.db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        helper.close();
    }

    public List<Viagem> listarViagem() {

        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA,
                DatabaseHelper.Viagem.COLUNAS,
                null, null, null, null, null);

        List<Viagem> viagens = new ArrayList<>();

        while (cursor.moveToNext()) {
            Viagem viagem = criarViagem(cursor);
            viagens.add(viagem);
        }

        return viagens;
    }

    private Viagem criarViagem(Cursor cursor) {
        Viagem viagem = new Viagem();
        viagem.setId(cursor.getLong(0));
        viagem.setTipoViagem(cursor.getInt(1));
        viagem.setDestino(cursor.getString(2));
        viagem.setDataChegada(new Date(cursor.getLong(3)));
        viagem.setDataSaida(new Date(cursor.getLong(4)));
        viagem.setOrcamento(cursor.getDouble(5));
        return viagem;
    }
}
