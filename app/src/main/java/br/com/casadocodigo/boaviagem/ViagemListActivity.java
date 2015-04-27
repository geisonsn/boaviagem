package br.com.casadocodigo.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by geison on 22/04/15.
 */
public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private DatabaseHelper helper;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private AlertDialog dialogConfirmacao;
    private Double valorLimite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this);

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};

        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(),
                R.layout.lista_viagem, de, para);

        /*setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.listarViagens()));*/

        adapter.setViewBinder(new ViagemViewBinder());
        setListAdapter(adapter);

        ListView listView = getListView();
        listView.setOnItemClickListener(this);

        this.alertDialog = criarAlertDialog();
        this.dialogConfirmacao = criarDialogConfirmacao();
    }

    public List<Map<String, Object>> listarViagens() {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT _id, tipo_viagem, destino, " +
            "data_chegada, data_saida, orcamento FROM viagem", null);

        cursor.moveToFirst();

        viagens = new ArrayList<>();

        for (int i = 0;  i < cursor.getCount(); i++) {
            Map<String, Object> item = new HashMap<>();
            String id = cursor.getString(0);
            int tipoViagem = cursor.getInt(1);
            String destino = cursor.getString(2);
            long dataChegadaLong = cursor.getLong(3);
            long dataSaidaLong = cursor.getLong(4);
            double orcamento = cursor.getDouble(5);

            item.put("id", id);

            if (tipoViagem == Constantes.VIAGEM_LAZER) {
                item.put("imagem", R.drawable.lazer);
            } else {
                item.put("imagem", R.drawable.negocios);
            }

            item.put("destino", destino);

            Date dataChegada = new Date(dataChegadaLong);
            Date dataSaida = new Date(dataSaidaLong);

            String periodo = dateFormat.format(dataChegada) + " a " + dateFormat.format(dataSaida);
            item.put("data", periodo);

            double totalGasto = calcularTotalGasto(db, id);

            item.put("total", "Gasto total R$ " + totalGasto);

            double alerta = orcamento * valorLimite / 100;
            Double[] valores = new Double[] {orcamento, alerta, totalGasto};
            item.put("barraProgresso", valores);

            viagens.add(item);

            cursor.moveToNext();
        }

        cursor.close();

        return viagens;
    }

    private double calcularTotalGasto(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery("SELECT SUM(valor) FROM gasto WHERE viagem_id = ?",
                new String[] {id});

        cursor.moveToFirst();

        double total = cursor.getDouble(0);

        cursor.close();

        return total;
    }

    /*private List<Map<String, Object>> listarViagens() {
        viagens = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.negocios);
        item.put("destino", "São Paulo");
        item.put("data", "02/02/2012 a 04/02/2012");
        item.put("total", "Gasto total R$ 314,98");
        item.put("barraProgresso", new Double[] {500.0, 450.0, 314.98});
        viagens.add(item);

        item = new HashMap<>();
        item.put("imagem", R.drawable.lazer);
        item.put("destino", "Maceió");
        item.put("data", "14/05/2012 a 22/05/2012");
        item.put("total", "Gasto total R$ 25834, 67");
        item.put("barraProgresso", new Double[] {3500.0, 2550.0, 314.98});
        viagens.add(item);

        return viagens;
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        /*Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");

        *//*TextView textView = (TextView) view;
        String mensagem = "Viagem selecionada: " + textView.getText();*//*

        String mensagem = "Viagem selecionada: " + destino;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListActivity.class));*/

        this.viagemSelecionada = position;
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int item) {

        Intent intent;

        String id = (String) viagens.get(viagemSelecionada).get("id");

        switch (item) {
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(this.viagemSelecionada);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }

    private AlertDialog criarAlertDialog() {
        final CharSequence[] items = {
            getString(R.string.editar),
            getString(R.string.novo_gasto),
            getString(R.string.gastos_realizados),
            getString(R.string.remover)
        };

        return new AlertDialog.Builder(this)
            .setTitle(R.string.opcoes)
            .setItems(items, this)
            .create();
    }

    private AlertDialog criarDialogConfirmacao() {
        return new AlertDialog.Builder(this)
            .setMessage(R.string.confirmacao_exclusao_viagem)
            .setPositiveButton(getString(R.string.sim), this)
            .setNegativeButton(getString(R.string.nao), this)
            .create();
    }

    private class ViagemViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if (view.getId() == R.id.barraProgresso) {
                Double[] valores = (Double[]) data;
                ProgressBar progressBar = (ProgressBar) view;
                progressBar.setMax(valores[0].intValue());
                progressBar.setSecondaryProgress(valores[1].intValue());
                progressBar.setProgress(valores[2].intValue());
                return true;
            }
            return false;
        }
    }
}
