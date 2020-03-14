package br.com.michel.android.organizzeclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import br.com.michel.android.organizzeclone.R;
import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textJatenhoumaconta);


    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void btnLogin (View view){
        startActivity(new Intent(this, Login.class));
    }
    public void btnCadastro (View view){
        startActivity(new Intent(this, Cadastro.class));
    }

    public void verificarUsuarioLogado(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();

        if (autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();

        }
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));


    }
}
