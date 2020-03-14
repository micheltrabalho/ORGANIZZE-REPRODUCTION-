package br.com.michel.android.organizzeclone.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;
import br.com.michel.android.organizzeclone.helper.Base64Custom;
import br.com.michel.android.organizzeclone.helper.DataUtil;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private String key;


    public Movimentacao() {
    }

    public void salvar(String dataEscolhida) {

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarbase64(auth.getCurrentUser().getEmail());
        String mesAno = DataUtil.mesAnoDataEscolhida(dataEscolhida);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebase();
        reference.child("movimentacao").child(idUsuario).child(mesAno).push().setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
