package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.com.casadocodigo.boaviagem.dao.GastoDAO;
import br.com.casadocodigo.boaviagem.domain.Gasto;

/**
 * Created by p001234 on 22/04/15.
 */
public class GastoActivity extends Activity {

    private GastoDAO dao;

    private int ano, mes, dia;
    private Button dataGastoButton;
    private Spinner categoria;
    private Gasto gasto = new Gasto();
    private String idViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

        inicializarData();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoria_gasto, android.R.layout.simple_spinner_item);

        categoria = (Spinner) findViewById(R.id.categoria);
        categoria.setAdapter(adapter);

        idViagem = getIntent().getStringExtra(Constantes.VIAGEM_ID);
    }

    private void inicializarData() {
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataGastoButton = (Button) findViewById(R.id.data);
        dataGastoButton.setText(dia + "/" + (mes + 1) + "/" + ano);

        gasto.setData(calendar.getTime());
    }

    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.data == id) {
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            ano = year;
            mes = month;
            dia = dayOfMonth;
            dataGastoButton.setText(dia + "/" + (mes + 1) + "/" + ano);

            Calendar calendar = Calendar.getInstance();

            gasto.setData(calendar.getTime());
        }
    };

    public void registrarGasto(View view) {
        Spinner categoria = (Spinner) findViewById(R.id.categoria);
        TextView valor = (TextView) findViewById(R.id.valor);
        TextView descricao = (TextView) findViewById(R.id.descricao);
        TextView local = (TextView) findViewById(R.id.local);

        gasto.setCategoria(categoria.getSelectedItem().toString());
        gasto.setValor(Double.valueOf(valor.getText().toString()));
        gasto.setDescricao(descricao.getText().toString());
        gasto.setLocal(local.getText().toString());

        dao = new GastoDAO(this);

        long resultado = dao.saveOrUpdate(gasto);

        if (resultado != -1) {
            Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
        }

        dao.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
