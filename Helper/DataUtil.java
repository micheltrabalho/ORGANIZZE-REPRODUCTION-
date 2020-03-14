package br.com.michel.android.organizzeclone.helper;

import java.text.SimpleDateFormat;

public class DataUtil {

    public static String dataAtual() {

        /** esse foi o metodo criado para capturar a data autal do sistema
         */

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MM/yyyy");
        String dataString = simpleDateFormat.format(date);
        return dataString;
    }

    public static String mesAnoDataEscolhida(String data) {

        String retornoData[] =  data.split("/");// metodo utilizado para retirar a barra da data, poderia ser qualquer caracter, nesse caso foi definida a barra
        String dia = retornoData[0];
        String mes = retornoData[1];
        String ano = retornoData[2];

        String mesSno =  mes + ano;
        return mesSno;
    }
}
