package br.com.michel.android.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.michel.android.organizzeclone.R;
import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;
import br.com.michel.android.organizzeclone.helper.Base64Custom;
import br.com.michel.android.organizzeclone.model.Usuario;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Cadastro extends AppCompatActivity {

    private Button btnCadastrar;
    private EditText nome, senha, email;
    private FirebaseAuth autenticacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().setTitle("Cadastro");//muda o nome na action bar

        nome = findViewById(R.id.editTextNome);
        senha = findViewById(R.id.editSenha);
        email = findViewById(R.id.editEmail);
        btnCadastrar = findViewById(R.id.buttonCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textonome = nome.getText().toString();
                String txtemail = email.getText().toString();
                String txtsenha = senha.getText().toString();

                // Validacao se os campos foram preenchidos
                if (!textonome.isEmpty()){
                    if (!txtemail.isEmpty()){
                        if (!txtsenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(textonome);
                            usuario.setEmail(txtemail);
                            usuario.setSenha(txtsenha);

                            cadastrarUsuario();

                        }
                        else {
                            Toast.makeText(Cadastro.this, "Preencha a Senha", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(Cadastro.this, "Preencha o E-mail", Toast.LENGTH_LONG).show();
                    }


                }
                else {
                    Toast.makeText(Cadastro.this, "Preencha o nome!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // criacao do metodo para efetuar o cadastro dos usuarios
    public void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String idUsuario = Base64Custom.codificarbase64(usuario.getEmail());// codificacao do email usando a classe criada
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar(); // ira usar o metodo salvar que foi criado la na classe Usuario

                    finish();
                }
                else {

                    String excecao = ""; // foi criado uma string vazia, abaixo vai ver se algumas das opcoes estao corretas
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) { // caso a senha seja fraca ira aparecer a menssagem para digitar uma mais forte
                        excecao = "Digite uma senha mais forte!";

                    } catch (FirebaseAuthInvalidCredentialsException e) { // caso o email nao esteja no formato correto ira a parecer a menssagem para digitar o email valido
                        excecao = "Digite um email valido";

                    } catch (FirebaseAuthUserCollisionException e) { // caso o email ja exista no banco de dados seja exibida a menssagem que ja foi cadastrado
                        excecao = "Essa conta ja foi cadastrada";

                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuario: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(Cadastro.this, excecao, Toast.LENGTH_LONG).show();
                }

                // mesmo estando tudo configurado aqui, se nao funcionar da uma olhada nas regrar la no site do firebase
                /**
                 * com essa regra abaixo ira funcionar
                 * {
                 "rules": {
                 ".read": "auth != null",
                 ".write": "auth != null"
                 }
                 }**/
            }
        });

    }
}
