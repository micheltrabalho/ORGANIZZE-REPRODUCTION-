package br.com.michel.android.organizzeclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.michel.android.organizzeclone.R;
import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;
import br.com.michel.android.organizzeclone.helper.Base64Custom;
import br.com.michel.android.organizzeclone.helper.DataUtil;
import br.com.michel.android.organizzeclone.model.Movimentacao;
import br.com.michel.android.organizzeclone.model.Usuario;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private EditText data, categoria, descricao;
    private TextView valor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitatotal;
    private Double receitagerada;
    private Double receitaatualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);
        data = findViewById(R.id.editText);
        categoria = findViewById(R.id.editText2);
        descricao = findViewById(R.id.editText3);
        valor = findViewById(R.id.textView6);


        //preencher data atual
        /** para usar a data atual é necessario criar uma classe, nesse caso a classe criada se chama "DataUtil", nela estao as confinguracoes
         * para que o sistema capte a data atual, no trecho a seguir ira exibir a data atual capturada pela classe "DataUtil", utilizando o
         * metodo ".dataAtual()"
         */

        data.setText(DataUtil.dataAtual());
        recuperarreceitatotal();

    }

    public void salvarReceita(View view){

        if (validarCamposReceita()){

            String Data = data.getText().toString();

            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(valor.getText().toString())); // configurou o valor da movimentacao

            movimentacao.setCategoria(categoria.getText().toString()); // configurou a categoria
            movimentacao.setDescricao(descricao.getText().toString()); // configurou a descricao
            movimentacao.setData(data.getText().toString()); // nesse caso a data e uma string
            movimentacao.setTipo("r"); // como e uma despesa ficara com a letra "d"

            receitagerada = Double.parseDouble(valor.getText().toString());
            receitaatualizada = receitatotal + receitagerada;
            atualizardespesa(receitaatualizada);

            movimentacao.salvar(Data);

            finish();
        }
    }

    public Boolean validarCamposReceita() {

        String textdata = data.getText().toString();
        String txtcategoria = categoria.getText().toString();
        String txtdescricao = descricao.getText().toString();
        String txtvalor = valor.getText().toString();

        if (!txtvalor.isEmpty()){
            if (!textdata.isEmpty()){
                if (!txtcategoria.isEmpty()){
                    if (!txtdescricao.isEmpty()){
                        return true;
                    }
                    else {
                        Toast.makeText(ReceitasActivity.this, "Preencha a Descricao", Toast.LENGTH_LONG).show();
                        return false;
                    }

                }
                else {
                    Toast.makeText(ReceitasActivity.this, "Preencha a Categoria", Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            else {
                Toast.makeText(ReceitasActivity.this, "Preencha a Data", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else {
            Toast.makeText(ReceitasActivity.this, "Preencha o Valor", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void recuperarreceitatotal() {
        String emailUsuario =  auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarbase64(emailUsuario);

        DatabaseReference usuarioref = firebaseref.child("usuarios").child(idUsuario);

        usuarioref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitatotal = usuario.getReceitatotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public  void atualizardespesa(Double receita) {
        String emailUsuario =  auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarbase64(emailUsuario);

        DatabaseReference usuarioref = firebaseref.child("usuarios").child(idUsuario);
        usuarioref.child("receitatotal").setValue(receita);
    }
}
