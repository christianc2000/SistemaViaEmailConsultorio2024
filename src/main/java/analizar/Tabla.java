/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizar;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Christian
 */
public class Tabla {

    public String nombre;
    public ArrayList<Atributo> atributos; //nombre,tipo_de_dato
    public int noNulos;
    public Tabla(String nombre, ArrayList<Atributo> atributos, int noNulos) {
        this.nombre = nombre;
        this.atributos = atributos;
        this.noNulos=noNulos;
    }

}
