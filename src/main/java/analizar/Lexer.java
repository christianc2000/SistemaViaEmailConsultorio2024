/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizar;

/**
 *
 * @author Christian
 */
import java.util.regex.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lexer {

    private Pattern patronStart = Pattern.compile("^START\\[\\];$");
    private Pattern patronHelp = Pattern.compile("^HELP\\[\\];$");
    private Pattern patronList = Pattern.compile("^LIST\\[[a-zA-Z0-9_]+\\];$");
    private Pattern patronListAttributes = Pattern.compile("^LIST\\[[a-zA-Z0-9_]+:(?:[a-zA-Z0-9_]+,)*[a-zA-Z0-9_]+\\];$");
    //private Pattern patronInsert = Pattern.compile("^INSERT\\[[a-zA-Z0-9_]+:(?:[a-zA-Z0-9_]+=\\w+,)*[a-zA-Z0-9_]+=\\w+\\];$");
    //private Pattern patronInsert = Pattern.compile("^INSERT\\[[a-zA-Z0-9_]+:(?:[a-zA-Z0-9_]+=[\\w\\-\\s]+,)*[a-zA-Z0-9_]+=[\\w\\-\\s]+\\];$");
    //private Pattern patronInsert = Pattern.compile("^INSERT\\[[a-zA-Z0-9_]+:(?:[a-zA-Z0-9_]+=[\\w\\-\\s@.]+,)*[a-zA-Z0-9_]+=[\\w\\-\\s@.]+\\];$");
    private Pattern patronInsert = Pattern.compile("^INSERT\\[[a-zA-Z0-9_]+:(?:[a-zA-Z0-9_]+=[\\w\\-\\s@./:]+,)*[a-zA-Z0-9_]+=[\\w\\-\\s@./:]+\\];$");
    private Pattern patronUpdate = Pattern.compile("^UPDATE\\[[a-zA-Z0-9_]+:(?:[a-zA-Z0-9_]+=[\\w\\-\\s@.]+,)*[a-zA-Z0-9_]+=[\\w\\-\\s@.]+\\];$");
    private Pattern patronShow = Pattern.compile("^SHOW\\[[a-zA-Z0-9_]+:[a-zA-Z0-9_]+=\\w+\\];$");
    private Pattern patronDelete = Pattern.compile("^DELETE\\[[a-zA-Z0-9_]+:[a-zA-Z0-9_]+=\\w+\\];$");

    public TablasBD tablasBD;

    public Lexer() {
        this.tablasBD = new TablasBD();
    }

    public Command lex(String input) {
        Command comando = new Command(null, null, null, null);
        Matcher matcher = patronStart.matcher(input);
        int pos = -1;
        if (matcher.matches()) {//START[]
            comando.accion = "START";
        } else if (patronHelp.matcher(input).matches()) {//HELP[]
            comando.accion = "HELP";
        } else if (patronList.matcher(input).matches()) {//LIST[table]
            comando.accion = "LIST";
            //analizar tabla
            String context = input.substring(input.indexOf("[") + 1, input.indexOf("]"));
            String tabla = getTable(context); //obtiene el campo tabla del input
            pos = analizarTabla(tabla); //analiza si la tabla es valida
            if (pos > -1) {//la tabla existe
                comando.tabla = tabla;
            } else {
                comando.error = "Error: Tabla no existe";
            }
        } else if (patronListAttributes.matcher(input).matches()) {//LIST[table:atr1,atr2,....]
            comando.accion = "LISTATRI";
            //analizar tabla
            String context = input.substring(input.indexOf("[") + 1, input.indexOf("]"));
            String tabla = getTable(context); //obtiene el campo tabla del input
            pos = analizarTabla(tabla); //analiza si la tabla es valida
            if (pos > -1) {//la tabla existe
                comando.tabla = tabla;
                //analizar atributo
                ArrayList<String> atributos = getAtributos(context);
                boolean succes = analizarAtributos(atributos, pos);
                boolean repetidos = atributosRepetidos(atributos);
                if (succes) {
                    if (!repetidos) {
                        comando.atributos = arrayListToMap(atributos);
                    } else {
                        comando.error = "Error: Atributos repetidos";
                    }
                } else {
                    comando.error = "Error: Atributos incorrecto";
                }
            } else {
                comando.error = "Error: Tabla no existe";
            }
        } else if (patronInsert.matcher(input).matches()) {
            comando = procesarComando(input, "INSERT");
        } else if (patronUpdate.matcher(input).matches()) {
            comando = procesarComando(input, "UPDATE");
        } else if (patronShow.matcher(input).matches()) {
            System.out.println("ingresa a show");
            comando = procesarComando(input, "SHOW");
        } else if (patronDelete.matcher(input).matches()) {
            comando = procesarComando(input, "DELETE");
        } else {
            comando.error = "Error: el comando '" + input + "' no coincide con ninguna sintaxis válida. Puede ver todos los comandos presionando HELP[];";
        }

        return comando;
    }
//****************************************************************************************************************************************

    public Command procesarComando(String input, String accion) {
        Command comando = new Command(null, null, null, null);
        int pos = -1;
        comando.accion = accion;
        // Analizar tabla
        String context = input.substring(input.indexOf("[") + 1, input.indexOf("]"));
        String tabla = getTable(context); // obtiene el campo tabla del input
        pos = analizarTabla(tabla); // analiza si la tabla es valida
        if (pos > -1) { // la tabla existe
            comando.tabla = tabla;
            // analizar atributo
            ArrayList<String[]> atributos = getAtributosValor(context);
            boolean succes = analizarAtributoValor(atributos, pos, accion.equals("INSERT") ? "I" : "U");
            boolean repetidos = atributosRepetidosValor(atributos);
            if (succes) {
                if (!repetidos) {
                    Map<String, Object> atri = arrayListToMapValor(atributos);
                    System.out.println("analizar TIPO DE DATOS");
                    String errores = analizarTipoDato(atri, pos);
                    System.out.println("convertir a TIPO DE DATOS");

                    if (errores.length() == 0) {
                        atri = convertirTipoDato(atri, pos);
                        comando.atributos = atri;
                    } else {
                        comando.error = errores;
                        System.out.println("************************DESDE PROCESAR COMANDO***********************");

                    }
                } else {
                    comando.error = "Error: Atributos repetidos";
                }
            } else {
                comando.error = "Error: Atributos incorrecto";
            }
        } else {
            comando.error = "Error: Tabla no existe";
        }
        return comando;
    }

    public int analizarTabla(String inputTable) {
        for (int i = 0; i < tablasBD.tablas.size(); i++) {
            Tabla tabla = tablasBD.tablas.get(i);
            if (tabla.nombre.equals(inputTable)) {
                return i;
            }
        }
        return -1;
    }

    public boolean analizarAtributos(ArrayList<String> atributos, int posTabla) {
        Tabla tabla = tablasBD.tablas.get(posTabla);
        //  System.out.println("tabla a analizar: " + tabla.nombre);
        ArrayList<Atributo> atributosBD = tabla.atributos;
        boolean bandera = false;
        for (int i = 0; i < atributos.size(); i++) {
            for (int j = 0; j < atributosBD.size(); j++) {
                //  System.out.println("atributo: " + atributos.get(i));
                //  System.out.println("atributo comparacion: " + atributosBD.get(j).nombre);
                if (atributos.get(i).equals(atributosBD.get(j).nombre)) {
                    //    System.out.println("ingresa if");
                    bandera = true;
                }
            }
            if (!bandera) {
                return false;
            } else {
                bandera = false;
            }
        }

        return true;
    }

    public boolean visitado(int v[], int n, int pos) {
        if (n < v.length) {
            for (int i = 0; i < n; i++) {
                if (v[i] == pos) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean analizarAtributoValor(ArrayList<String[]> atributos, int posTabla, String tipo) {
        Tabla tabla = tablasBD.tablas.get(posTabla);
        System.out.println("tabla a analizar: " + tabla.nombre);
        ArrayList<Atributo> atributosBD = tabla.atributos;
        boolean bandera = false;
        boolean pk = false;
        int visitados[] = new int[tabla.noNulos];
        int n = 0;

        for (int i = 0; i < atributos.size(); i++) {
            String atr = atributos.get(i)[0];
            for (int j = 0; j < atributosBD.size(); j++) {
                if (atr.equals(atributosBD.get(j).nombre)) {
                    //System.out.println("es igual atr: " + atributos.get(i)[0] + ", atrc: " + atributosBD.get(j).nombre);
                    if (!visitado(visitados, n, j)) {
                        if (!atributosBD.get(j).nulo) {
                            //System.out.println("atributo no nulo: " + atributosBD.get(j).nombre);
                            n++;
                            visitados[n - 1] = j;
                        } else if (tipo.equals("I") && (atributosBD.get(j).llave != null)) {
                            if (atributosBD.get(j).llave.equals("PK")) {
                                bandera = false;
                                break;
                            }
                        } else if ((tipo.equals("U") || tipo.equals("S") || tipo.equals("D")) && (atributosBD.get(j).llave != null)) {
                            if (atributosBD.get(j).llave.equals("PK")) {
                                //  System.out.println("ingresa al pk del update");
                                pk = true;
                                bandera = true;
                                break;
                            }
                        } else {
                            //System.out.println("atributo nulo: " + atributosBD.get(j).nombre);
                        }
                        bandera = true;
                        break;
                    }
                }
            }
            if (!bandera) {
                return false;
            } else {
                bandera = false;
            }
        }
        if (tipo.equals("I")) {
            if (n < tabla.noNulos) {
                //  System.out.println("ingresa n<nonulos");
                return false;
            }
        }

        //verifica si en las acciones que requiere el pk se encontró
        if (tipo.equals("U") || tipo.equals("S") || tipo.equals("D")) {
            // System.out.println("final U");
            if (!pk) {
                return false;
            }
        }
        return true;
    }

    public String analizarTipoDato(Map<String, Object> atributoValor, int posTabla) {
        Tabla tabla = tablasBD.tablas.get(posTabla);
        ArrayList<Atributo> atributos = tabla.atributos;
        ArrayList<String> keys = new ArrayList<>(atributoValor.keySet());
        String errores = "";
        for (Atributo atributo : atributos) {
            for (int i = 0; i < keys.size(); i++) {
                if (atributo.nombre.equals(keys.get(i))) {
                    String error = esTipoDato(atributoValor.get(keys.get(i)).toString(), atributo.tipoDato);
                    if (error.length() > 0) {
                        errores = errores + error + "\n";
                        //System.out.println(errores);
                    } else {
                        System.out.println("No hay error");
                        keys.remove(i);
                        break;
                    }

                }
            }
        }
        return errores;
    }

    public Map<String, Object> convertirTipoDato(Map<String, Object> atributoValor, int posTabla) {
        Tabla tabla = tablasBD.tablas.get(posTabla);
        ArrayList<Atributo> atributos = tabla.atributos;
        ArrayList<String> keys = new ArrayList<>(atributoValor.keySet());
        String errores = "";
        for (Atributo atributo : atributos) {
            for (int i = 0; i < keys.size(); i++) {
                if (atributo.nombre.equals(keys.get(i))) {
                    try {
                        Object newValor = convertirTD(atributoValor.get(keys.get(i)).toString(), atributo.tipoDato);
                        atributoValor.put(keys.get(i), newValor);  // Actualiza el valor en el mapa
                    } catch (ParseException ex) {
                        Logger.getLogger(Lexer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return atributoValor;

    }

    public Object convertirTD(String dato, String tipoDato) throws ParseException {
        switch (tipoDato) {
            case "string":
                return dato;
            case "email":
                if (dato.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$")) {
                    return dato;
                } else {
                    throw new IllegalArgumentException("El valor no es un email válido: " + dato);
                }
            case "int":
                return Integer.valueOf(dato);
            case "float":
                return Float.valueOf(dato);
            case "double":
                return Double.valueOf(dato);
            case "boolean":
                if (dato.equalsIgnoreCase("true") || dato.equalsIgnoreCase("false")) {
                    return Boolean.valueOf(dato);
                } else {
                    throw new IllegalArgumentException("El valor no es un booleano válido: " + dato);
                }
            case "date":
                return dato;
            case "time":
                return dato;
            case "datetime":
                return dato;
            default:
                throw new IllegalArgumentException("Tipo de dato no válido: " + tipoDato);
        }
    }

    public String esTipoDato(String dato, String tipoDato) {
        try {
            switch (tipoDato) {
                case "string":
                    //System.out.println(dato + " es " + tipoDato);
                    return "";
                case "email":
                    //System.out.println(dato + " es " + tipoDato);
                    if (dato.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$")) {
                        return "";
                    }
                    break;
                case "int":
                    int v = Integer.valueOf(dato);
                    //System.out.println(dato + " es " + v);
                    return "";
                case "float":
                    Float.valueOf(dato);
                    //System.out.println(dato + " es " + tipoDato);
                    return "";
                case "double":
                    Double.valueOf(dato);
                    //System.out.println(dato + " es " + tipoDato);
                    return "";
                case "boolean":
                    if (dato.equalsIgnoreCase("true") || dato.equalsIgnoreCase("false")) {
                        //System.out.println(dato + " es " + tipoDato);
                        return "";
                    }
                    break;
                case "date":
                    new SimpleDateFormat("yyyy-MM-dd").parse(dato);
                    //System.out.println(dato + " es " + tipoDato);
                    return "";
                case "time":
                    new SimpleDateFormat("HH:mm:ss").parse(dato);
                    //System.out.println(dato + " es " + tipoDato);
                    return "";
                case "datetime":
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dato);
                    //System.out.println(dato + " es " + tipoDato);
                    return "";
                default:
                    throw new AssertionError();
            }
        } catch (NumberFormatException | ParseException e) {
            return "Error: " + e.getMessage() + ". El dato "+dato+" debe tener el formato del tipo de dato '" + tipoDato + "'.";
        }
        return "Tipo de dato no válido: " + tipoDato;
    }

    //parsear tabla
    public String getTable(String input) {
        int p = input.indexOf(":");
        if (p != -1) {
            return input.substring(0, p);
        }
        return input;
    }

    //parsear atributos
    public ArrayList<String> getAtributos(String input) {
        ArrayList<String> atributo = new ArrayList<>();
        String contexto = input.substring(input.indexOf(":") + 1);
        String dato = "";
        for (int i = 0; i < contexto.length(); i++) {
            if (contexto.charAt(i) != ',') {
                dato = dato + contexto.charAt(i);
            } else {
                atributo.add(dato);
                dato = "";
            }
        }
        atributo.add(dato);
        System.out.println("atributos orden de guardado en getAtributos del LISTATRI");
        for (int i = 0; i < atributo.size(); i++) {
            System.out.print(", " + atributo.get(i));
        }
        System.out.println("");
        return atributo;
    }

    public boolean atributosRepetidos(ArrayList<String> atributos) {
        int c = 0;
        for (int i = 0; i < atributos.size(); i++) {
            for (int j = 0; j < atributos.size(); j++) {
                if (atributos.get(i).equals(atributos.get(j))) {
                    c++;
                }
            }
            if (c > 1) {
                return true;
            }
            c = 0;
        }
        return false;
    }

    public Map<String, Object> arrayListToMap(ArrayList<String> atributos) {
        Map<String, Object> atributo = new LinkedHashMap<>();
        for (int i = 0; i < atributos.size(); i++) {
            atributo.put(atributos.get(i), null);
        }
        return atributo;
    }
//*************Parsear Atributo Valor

    public ArrayList<String[]> getAtributosValor(String input) {
        ArrayList<String[]> atributo = new ArrayList<>();
        String contexto = input.substring(input.indexOf(":") + 1);
        String dato = "";
        for (int i = 0; i < contexto.length(); i++) {
            if (contexto.charAt(i) != ',') {
                dato = dato + contexto.charAt(i);
            } else {
                atributo.add(dato.split("="));
                dato = "";
            }
        }
        atributo.add(dato.split("="));
        return atributo;
    }

    public boolean atributosRepetidosValor(ArrayList<String[]> atributos) {
        int c = 0;
        for (int i = 0; i < atributos.size(); i++) {
            for (int j = 0; j < atributos.size(); j++) {
                if (atributos.get(i)[0].equals(atributos.get(j)[0])) {
                    c++;
                }
            }
            if (c > 1) {
                return true;
            }
            c = 0;
        }
        return false;
    }

    public Map<String, Object> arrayListToMapValor(ArrayList<String[]> atributos) {
        Map<String, Object> atributo = new LinkedHashMap<>();
        for (int i = 0; i < atributos.size(); i++) {
            atributo.put(atributos.get(i)[0], atributos.get(i)[1]);
        }
        return atributo;
    }

    public Object convertToTipoDato(String key, String dato, int pos) {
        System.out.println("convertir de la key: " + key + " el dato: " + dato);
        Tabla tabla = tablasBD.tablas.get(pos);
        ArrayList<Atributo> atributosBD = tabla.atributos;
        for (int i = 0; i < atributosBD.size(); i++) {
            if (atributosBD.get(i).nombre.equals(key)) {
                try {
                    switch (atributosBD.get(i).tipoDato) {
                        case "string":
                            return dato;
                        case "email":
                            return dato;
                        case "int":
                            return Integer.valueOf(dato);
                        case "float":
                            return Float.valueOf(dato);
                        case "double":
                            return Double.valueOf(dato);
                        case "boolean":
                            if (dato.equalsIgnoreCase("true")) {
                                return true;
                            } else {
                                return false;
                            }
                        case "date":
                            System.out.println("intenta convertir en date el " + dato);
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dato);
                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            System.out.println("Fecha formateada: " + formattedDate);
                            return formattedDate;
                        case "time":
                            return new SimpleDateFormat("HH:mm:ss").parse(dato);
                        case "datetime":
                            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dato);
                        default:
                            throw new AssertionError();
                    }
                } catch (NumberFormatException | ParseException e) {
                    return dato;
                }
            }
        }
        return dato;
    }
}
