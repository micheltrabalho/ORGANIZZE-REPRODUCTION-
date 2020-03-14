package br.com.michel.android.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.michel.android.organizzeclone.R;
import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;
import br.com.michel.android.organizzeclone.model.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Login extends AppCompatActivity {

    private Button button;
    private EditText senha, email;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.buttonEntrar);
        senha = findViewById(R.id.editSenha);
        email = findViewById(R.id.editEmail);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtemail = email.getText().toString();
                String txtsenha = senha.getText().toString();

                if (!txtemail.isEmpty()){
                    if (!txtsenha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(txtemail);
                        usuario.setSenha(txtsenha);
                        validarLogin();

                    }
                    else {
                        Toast.makeText(Login.this, "Preencha o campo Senha!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(Login.this, "Preencha o campo E-mail!", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    public void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    abrirTelaPrincipal();
                }
                else {

                    String exception = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "E-mail e senha não correspondem ao usuário cadastrado";
                    } catch (FirebaseAuthInvalidUserException e){
                        exception = "Usuário não esta cadastrado";
                    } catch (Exception e){
                        exception = "Erro ao " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(Login.this, exception, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }


}
