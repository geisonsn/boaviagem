package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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
    private Calendar dataChegada;
    private Calendar dataSaida;
    private int ano;
    private int mes;
    private int dia;
    private static Map<Integer, Calendar> datas = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        datas.put(R.id.dataChegada, dataChegada);
        datas.put(R.id.dataSaida, dataSaida);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        dataSaidaButton.setText(dia + "/" + (mes + 1) + "/" + ano);

        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        helper = new DatabaseHelper(this);

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
//        values.put("data_chegada", dataChegada.getTim);
        values.put("destino", destino.getText().toString());
    }

    public void selecionaData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.dataChegada == id) {
            return new DatePickerDialog(this, new OnDateSetListener(id), ano, mes, dia);
        } else if (R.id.dataSaida == id) {
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

            Calendar data = datas.get(this.viewId);
            data = Calendar.getInstance();
            data.set(year, monthOfYear, dayOfMonth);
            datas.put(this.viewId, data);
            botao.setText(dataString);
        }
    }
}
