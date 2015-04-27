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
}
