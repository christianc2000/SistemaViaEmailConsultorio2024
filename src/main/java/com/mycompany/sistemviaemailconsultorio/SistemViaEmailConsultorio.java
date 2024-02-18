/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.sistemviaemailconsultorio;

import analizar.Command;
import analizar.Lexer;
import java.util.List;

/**
 *
 * @author Christian
 */
public class SistemViaEmailConsultorio {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        //String input="START[];";
        //String input="HELP[];";
        //String input="LIST[users];";
        //String input="LIST[users:id,ci,name];";
        String input = "INSERT[users:ci=9821736,name=Christian Celso,lastname=Mamani Soliz,birth_date=2000-01-04,genero=M,celular=77376902,tipo=M,residencia_actual=Santa Cruz,sueldo=4000,email=chrstncelso@gmail.com,password=9821736,url_foto=fotografia.png];";
        //String input = "UPDATE[users:id=1,birth_date=2000-01-04,genero=M,celular=77376902,tipo=M,residencia_actual=Santa Cruz,email=chrstncelso@gmail.com,password=9821736,url_foto=fotografia.png];";
        //String input="INSERT[dias:nombre=Sabadingo];";
        //String input="SHOW[dias:id=1];";
        //String input = "DELETE[dias:id=1];";
        Lexer sintaxis = new Lexer();
        Command comando = sintaxis.lex(input);
        if (comando.error!=null) {
            System.out.println("Errores\n");
            System.out.println(comando.error);
        } else {
            System.out.println("Acción: " + comando.accion);
            System.out.println("Tabla: " + comando.tabla);
            System.out.println("Atributos: " + comando.atributos);
        }

        /*System.out.println("Cantidad de tokens encontrados: " + tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println("Tipo: " + tokens.get(i).getType());
            System.out.println("Valor: " + tokens.get(i).getValue());
        }*/
    }

}
