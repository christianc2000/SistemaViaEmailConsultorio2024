/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manejadorMensaje;

/**
 *
 * @author Christian
 */
public class ResponseBD {
    private String[][] data;
    private String error;
    
    public ResponseBD(String data[][], String error){
        this.data=data;
        this.error=error;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
