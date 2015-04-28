package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by p001234 on 22/04/15.
 */
public class ViagemActivity extends Activity {

    private DatabaseHelper helper;
    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;
    private Button dataChegadaButton;
    private Button dataSaidaButton;
    //private Calendar dataChegada;
    //private Calendar dataSaida;
    private int ano;
    private int mes;
    private int dia;
    private static Map<Integer, Calendar> datas = new HashMap<>();
    private String id;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        //datas.put(R.id.dataChegada, dataChegada);
        //datas.put(R.id.dataSaida, dataSaida);

        inicializarDatas(); //Inicializa as datas de chegada e saída

        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        helper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);
        if (id != null) {
            prepararEdicao();
        }
    }

    private void inicializarDatas() {
        Calendar calendar = Calendar.getInstance();
        this.ano = calendar.get(Calendar.YEAR);
        this.mes = calendar.get(Calendar.MONTH);
        this.dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        dataSaidaButton.setText(dia + "/" + (mes + 1) + "/" + ano);

        datas.put(R.id.dataChegada, Calendar.getInstance());
        datas.put(R.id.dataSaida, Calendar.getInstance());
    }

    private void prepararEdicao() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT tipo_viagem, destino, data_chegada, " +
                "data_saida, quantidade_pessoas, orcamento " +
                "FROM viagem WHERE _id = ?", new String[]{id});

        cursor.moveToFirst();

        if (cursor.getInt(0) == Constantes.VIAGEM_LAZER) {
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }

        destino.setText(cursor.getString(1));

        Calendar data = Calendar.getInstance();

        data.setTimeInMillis(cursor.getLong(2));
        datas.put(R.id.dataChegada, data);

        data = Calendar.getInstance();
        data.setTimeInMillis(cursor.getLong(3));
        datas.put(R.id.dataSaida, data);

        dataChegadaButton.setText(dateFormat.format(datas.get(R.id.dataChegada).getTime()));
        dataSaidaButton.setText(dateFormat.format(datas.get(R.id.dataSaida).getTime()));

        quantidadePessoas.setText(cursor.getString(4));
        orcamento.setText(cursor.getString(5));

        cursor.close();
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            case R.id.remover:
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    public void salvarViagem(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destino", destino.getText().toString());
        values.put("data_chegada", datas.get(R.id.dataChegada).getTime().getTime());
        values.put("data_saida", datas.get(R.id.dataSaida).getTime().getTime());
        values.put("quantidade_pessoas", quantidadePessoas.getText().toString());
        values.put("orcamento", orcamento.getText().toString());

        int tipo = radioGroup.getCheckedRadioButtonId();

        if (tipo == R.id.lazer) {
            values.put("tipo_viagem", Constantes.VIAGEM_LAZER);
        } else {
            values.put("tipo_viagem", Constantes.VIAGEM_NEGOCIOS);
        }

        long resultado = -1;
        if (id == null) {
            resultado = db.insert("viagem", null, values);
        } else {
            resultado = db.update("viagem", values, "_id = ?", new String[] {id});
        }

        if (resultado != -1) {
            Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
        }
    }

    public void selecionaData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.dataChegada == id) {

            if (this.id != null) {
                ano = datas.get(R.id.dataChegada).get(Calendar.YEAR);
                mes = datas.get(R.id.dataChegada).get(Calendar.MONTH);
                dia = datas.get(R.id.dataChegada).get(Calendar.DAY_OF_MONTH);
            }

            return new DatePickerDialog(this, new OnDateSetListener(id), ano, mes, dia);
        } else if (R.id.dataSaida == id) {

            if (this.id != null) {
                ano = datas.get(R.id.dataSaida).get(Calendar.YEAR);
                mes = datas.get(R.id.dataSaida).get(Calendar.MONTH);
                dia = datas.get(R.id.dataSaida).get(Calendar.DAY_OF_MONTH);
            }

            return new DatePickerDialog(this, new OnDateSetListener(id), ano, mes, dia);
        }
        return null;
    }

    private class OnDateSetListener implements DatePickerDialog.OnDateSetListener {

        private int viewId;

        public OnDateSetListener(int viewId) {
            this.viewId = viewId;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String dataString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            Button botao = (Button) findViewById(this.viewId);

//            Calendar data = datas.get(this.viewId);
            Calendar data = Calendar.getInstance();
            data.set(year, monthOfYear, dayOfMonth);
            datas.put(this.viewId, data);
            botao.setText(dataString);
        }
    }
}
