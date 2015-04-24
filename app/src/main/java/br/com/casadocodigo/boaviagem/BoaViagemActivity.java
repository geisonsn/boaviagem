package br.com.casadocodigo.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class BoaViagemActivity extends Activity {

    public static final String MINHAS_PREFERENCIAS = "MinhasPreferencias";
    public static final String MANTER_CONECTADO = "manter_conectado";
    private EditText usuario;
    private EditText senha;
    private CheckBox manterConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
        manterConectado = (CheckBox) findViewById(R.id.manterConectado);

        //SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        //boolean conectado = preferencias.getBoolean(MANTER_CONECTADO, false);

        boolean conectado = getSharedPreferences(BoaViagemActivity.MINHAS_PREFERENCIAS, MODE_PRIVATE).getBoolean(BoaViagemActivity.MANTER_CONECTADO, false);

        if (conectado) {
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }

    @Override
    protected void onRestart() {
//        Toast.makeText(this, "OnRestart BoaViagemActivity.class", Toast.LENGTH_SHORT).show();
        super.onRestart();
//        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
//        boolean conectado = preferencias.getBoolean(MANTER_CONECTADO, false);

        boolean conectado = getSharedPreferences(BoaViagemActivity.MINHAS_PREFERENCIAS, MODE_PRIVATE).getBoolean(BoaViagemActivity.MANTER_CONECTADO, false);
        if (conectado) {
            finish();
        }
    }

    @Override
    protected void onStart() {
//        Toast.makeText(this, "OnStart BoaViagemActivity.class", Toast.LENGTH_SHORT).show();
        super.onStart();
    }

    @Override
    protected void onResume() {
//        Toast.makeText(this, "OnResume BoaViagemActivity.class", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    protected void onPause() {
//        Toast.makeText(this, "OnPause BoaViagemActivity.class", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onStop() {
//        Toast.makeText(this, "OnStop BoaViagemActivity.class", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        Toast.makeText(this, "OnDestroy BoaViagemActivity.class", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_boa_viagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void entrarOnClick(View view) {
        String usuarioInformado  = usuario.getText().toString();
        String senha = this.senha.getText().toString();
        if ("usuario".equals(usuarioInformado) &&
                "123".equals(senha)) {

            //Salvando preferências do usuário
//            SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
            SharedPreferences preferencias = getSharedPreferences(MINHAS_PREFERENCIAS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putBoolean(MANTER_CONECTADO, manterConectado.isChecked());
            editor.commit();

            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            String mensagemErro = getString(R.string.erro_autenticacao);
            Toast.makeText(this, mensagemErro, Toast.LENGTH_SHORT).show();
        }
    }
}
