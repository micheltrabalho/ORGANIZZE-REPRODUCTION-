package br.com.michel.android.organizzeclone.config;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    //metodo que retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebase(){
        if (firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();// mesmo estando tudo configurado aqui, se nao funcionar da uma olhada nas regrar la no site do firebase
            /**
             * com essa regra abaixo ira funcionar
             * {
            "rules": {
                ".read": "auth != null",
                        ".write": "auth != null"
            }
        }**/
        }
         return firebase;
    }

    //metodo que retorna a instacia do Firebaseauth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;


    }
}
