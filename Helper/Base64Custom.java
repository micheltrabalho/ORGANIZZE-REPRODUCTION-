package br.com.michel.android.organizzeclone.helper;

import android.util.Base64;

public class Base64Custom {

    //essa classe foi criada para codificar e descodificar o email para o firebase

    public static String codificarbase64(String texto){

        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", ""); // \\n e \\r foram colocados para que na hora da codificao remova os espacos em branco e as quebras de linhas

    }

    public static String decodificarbase64(String textoCodificado){

        return new String (Base64.decode(textoCodificado, Base64.DEFAULT));

    }
}
