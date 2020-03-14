package br.com.michel.android.organizzeclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.michel.android.organizzeclone.R;
import br.com.michel.android.organizzeclone.adapter.AdapterMovimentacao;
import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;
import br.com.michel.android.organizzeclone.helper.Base64Custom;
import br.com.michel.android.organizzeclone.model.Movimentacao;
import br.com.michel.android.organizzeclone.model.Usuario;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView txtsaldo, txtsaudacao;
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebase();
    private DatabaseReference usuarioref;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovi;
    private Double despesatotal = 0.0;
    private Double receitatotal = 0.0;
    private Double resumousuario = 0.0;
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List <Movimentacao> movimentacoes = new ArrayList<>();
    private Movimentacao movimentacao;
    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        txtsaldo = findViewById(R.id.textSaldo);
        txtsaudacao = findViewById(R.id.textSaudacao);
        recyclerView = findViewById(R.id.recyclerView);
        calendarView = findViewById(R.id.calendarView);

        configuracalendarview();
        swipe();

        adapterMovimentacao = new AdapterMovimentacao(movimentacoes,this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);


    }
    // criacao do metodo swipe
    public void swipe() {

        ItemTouchHelper.Callback itemtouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int draflag = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeflag = ItemTouchHelper.START;
                return makeMovementFlags(draflag, swipeflag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemtouch).attachToRecyclerView(recyclerView);

    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        alertdialog.setTitle("Excluir Movimentacao da conta");
        alertdialog.setMessage("Tem certeza que deseja excluir?");
        alertdialog.setCancelable(false);

        alertdialog.setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);
                String emailUsuario =  auth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarbase64(emailUsuario);

                movimentacaoRef = firebaseref.child("movimentacao").child(idUsuario).child(mesAnoSelecionado);
                movimentacaoRef.child(movimentacao.getKey()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();

            }
        });

        alertdialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_LONG).show();
                adapterMovimentacao.notifyDataSetChanged();

            }
        });

        AlertDialog alertDialog = alertdialog.create();
        alertDialog.show();
    }

    public void atualizarSaldo() {


        if (movimentacao.getTipo().equals("r")){
            receitatotal = receitatotal - movimentacao.getValor();
            String emailUsuario =  auth.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarbase64(emailUsuario);
            usuarioref = firebaseref.child("usuarios").child(idUsuario);
            usuarioref.child("receitatotal").setValue(receitatotal);
        }

        if (movimentacao.getTipo().equals("d")){
            despesatotal = despesatotal - movimentacao.getValor();
            String emailUsuario =  auth.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarbase64(emailUsuario);
            usuarioref = firebaseref.child("usuarios").child(idUsuario);
            usuarioref.child("despesatotal").setValue(despesatotal);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // adicionando funcao ao toolbar menu
        switch (item.getItemId()){
            case R.id.menusair:

                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void recuperarMovimentacoes(){

        String emailUsuario =  auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarbase64(emailUsuario);

        movimentacaoRef = firebaseref.child("movimentacao").child(idUsuario).child(mesAnoSelecionado);

        valueEventListenerMovi = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                movimentacoes.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){

                 Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                 movimentacao.setKey(dados.getKey());
                 movimentacoes.add(movimentacao);


                }
                adapterMovimentacao.notifyDataSetChanged();//notificar que os dados foram modificados

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void recuperarresumo() {

        String emailUsuario =  auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarbase64(emailUsuario);

        usuarioref = firebaseref.child("usuarios").child(idUsuario);

        valueEventListenerUsuario = usuarioref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario  usuario = dataSnapshot.getValue(Usuario.class);
                despesatotal = usuario.getDespesatotal();
                receitatotal = usuario.getReceitatotal();
                resumousuario = receitatotal - despesatotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String rformatado = decimalFormat.format(resumousuario);

                txtsaldo.setText("R$" + rformatado);
                txtsaudacao.setText("Ola, " + usuario.getNome());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(this, DespesasActivity.class));

    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(this, ReceitasActivity.class));

    }

    public void configuracalendarview() {
        CharSequence meses [] = {"Janeiro", "Fevereiro","Marco", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(meses);

        CalendarDay day = calendarView.getCurrentDate();

        String mesSelecionado = String.format("%02d",day.getMonth() + 1);
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + day.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d",date.getMonth() + 1);
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                movimentacaoRef.removeEventListener(valueEventListenerMovi);
                recuperarMovimentacoes();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarresumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() { // metodo criado para fazer com que o app nao fique atualizando sempre, ira atualiza apenas quando estiver na pg principal
        super.onStop();
        usuarioref.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovi);
    }
}
