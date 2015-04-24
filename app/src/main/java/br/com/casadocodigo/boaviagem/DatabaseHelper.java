package br.com.casadocodigo.boaviagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by p001234 on 24/04/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "BoaViagem";
    private static final int VERSAO = 1;


    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer createViagem = new StringBuffer();
        createViagem
            .append(" CREATE TABLE viagem ")
            .append(" (_id INTEGER PRIMARY KEY,  ")
            .append(" destino TEXT, tipo_viagem INTEGER, data_chegada DATE, ")
            .append(" data_saida DATE, orcamento DOUBLE, ")
            .append(" quantidade_pessoas INTEGER); ");

        db.execSQL(createViagem.toString());

        StringBuffer createGasto = new StringBuffer();
        createGasto
            .append(" CREATE TABLE gasto ")
            .append(" (_id INTEGER PRIMARY_KEY, ")
            .append(" categoria TEXT, data DATE, valor DOUBLE ")
            .append(" descricao TEXT, local TEXT, viagem_id INTEGER, ")
            .append(" FOREIGN KEY(viagem_id) REFERENCES viagem(_id)); ");

        db.execSQL(createGasto.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
