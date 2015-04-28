package br.com.casadocodigo.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.casadocodigo.boaviagem.dao.GastoDAO;
import br.com.casadocodigo.boaviagem.domain.Gasto;

/**
 * Created by geison on 22/04/15.
 */
public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private GastoDAO dao;

    private List<Map<String,Object>> gastos;
    private String idViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"data", "descricao", "valor", "categoria"};
        int[] para = {R.id.data, R.id.descricao, R.id.valor, R.id.categoria};

        SimpleAdapter adapter = new SimpleAdapter(this, listarGastos(), R.layout.lista_gasto,
                de, para);

        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);

        /*setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listarGastos()));*/

        ListView listView = getListView();
        listView.setOnItemClickListener(this);

        registerForContextMenu(listView);

        idViagem = getIntent().getStringExtra(Constantes.VIAGEM_ID);
    }

    private List<Map<String, Object>> listarGastos() {

        dao = new GastoDAO(this);

        List<Gasto> listGastos = dao.listarGasto(idViagem);

        gastos = new ArrayList<Map<String, Object>>();

        for (Gasto gasto : listGastos) {
            Map<String, Object> item = new HashMap<>();
            item.put("data", gasto.getData());
            item.put("descricao", gasto.getDescricao());
            item.put("valor", gasto.getValor());
            item.put("categoria", R.color.categoria_hospedagem);
            gastos.add(item);
        }

        dao.close();

        return gastos;
    }

    private List<Map<String, Object>> listarGastosOLD() {
        gastos = new ArrayList<>();

        Map<String, Object> item = new HashMap<>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Diária Hotel");
        item.put("valor", "R$ 260,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);

        item = new HashMap<>();
        item.put("data", "07/02/2012");
        item.put("descricao", "Diária Hotel");
        item.put("valor", "R$ 160,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);

        item = new HashMap<>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Almoço");
        item.put("valor", "R$ 80,00");
        item.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item);

        item = new HashMap<>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Táxi");
        item.put("valor", "R$ 50,00");
        item.put("categoria", R.color.categoria_transporte);
        gastos.add(item);

        item = new HashMap<>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Rvista");
        item.put("valor", "R$ 20,00");
        item.put("categoria", R.color.categoria_outros);
        gastos.add(item);

//      return Arrays.asList("Sandúiche R$ 19,90", "Táxi Aeroporto - Hotel R$ 34,90", "Revista R$ 12,00");
        return gastos;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        TextView textView = (TextView) view;

        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descricao");
        String mensagem = "Gasto selecionado: " + descricao;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private String dataAnterior = "";
    private class GastoViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {

            if (view.getId() == R.id.data) {
                if (!dataAnterior.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }

            if (view.getId() == R.id.categoria) {
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remover) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            gastos.remove(info.position);
            removerGasto();
            getListView().invalidateViews();
            dataAnterior = "";
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void removerGasto() {

    }
}
