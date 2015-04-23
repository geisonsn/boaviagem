package br.com.casadocodigo.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by geison on 22/04/15.
 */
public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private int viagemSelecionada;
    private AlertDialog dialogConfirmacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private List<Map<String, Object>> listarViagens() {
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
    }

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
        switch (item) {
            case 0:
                startActivity(new Intent(this, ViagemActivity.class));
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