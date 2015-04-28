package br.com.casadocodigo.boaviagem.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.casadocodigo.boaviagem.DatabaseHelper;

/**
 * Created by p001234 on 27/04/15.
 */
public class BoaViagemDAO {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public BoaViagemDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getDb() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        helper.close();
    }

    /*public List<Viagem> listViagem() {
        Cursor cursor = getDb().query(DatabaseHelper.Viagem.TABELA,
                DatabaseHelper.Viagem.COLUNAS,
                null, null, null, null, null);
        List<Viagem> viagens = new ArrayList<>();
        while (cursor.moveToNext()) {
            Viagem viagem = criarViagem(cursor);
        }
    }*/
}
