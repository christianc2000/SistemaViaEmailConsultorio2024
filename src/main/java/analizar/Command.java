/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizar;

import java.util.Map;

/**
 *
 * @author Christian
 */
public class Command {
    public String accion;
    public String tabla;
    public Map<String, Object> atributos;
        public String error;

    public Command(String accion, String tabla, Map<String, Object> atributos, String error) {
        this.accion = accion;
        this.tabla = tabla;
        this.atributos = atributos;
        this.error = error;
    }

   
    
   
}
