package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by p001234 on 22/04/15.
 */
public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

    }

    public void selecionarOpcao(View view) {
        switch (view.getId()) {
            case R.id.nova_viagem :
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case R.id.minhas_viagens :
                startActivity(new Intent(this, ViagemListActivity.class));
                break;
            case R.id.configuracoes:
                startActivity(new Intent(this, ConfiguracoesActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish();
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
//        boolean conectado = preferencias.getBoolean(MANTER_CONECTADO, false);
        /*boolean conectado = getSharedPreferences(BoaViagemActivity.MINHAS_PREFERENCIAS, MODE_PRIVATE).getBoolean(BoaViagemActivity.MANTER_CONECTADO, false);
        if (conectado) {
            fecharAplicacao();
        }*/
    }

    //Má prática adiciona
    public void fecharAplicacao() {
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_HOME);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }
}
