package br.com.michel.android.organizzeclone.model;

import com.google.firebase.database.DatabaseReference;

import br.com.michel.android.organizzeclone.config.ConfiguracaoFirebase;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private Double receitatotal = 0.00;
    private Double despesatotal = 0.00;

    public Usuario() {

    }
     //ESTA EXCLUINDO ESSA INVORMACAO NAO IRA SALVAR NO BANDO DE DADOS
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("usuarios").child(this.idUsuario).setValue(this);
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

    public Double getReceitatotal() {
        return receitatotal;
    }

    public void setReceitatotal(Double receitatotal) {
        this.receitatotal = receitatotal;
    }

    public Double getDespesatotal() {
        return despesatotal;
    }

    public void setDespesatotal(Double despesatotal) {
        this.despesatotal = despesatotal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //ESTA EXCLUINDO ESSA INVORMACAO NAO IRA SALVAR NO BANDO DE DADOS
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
