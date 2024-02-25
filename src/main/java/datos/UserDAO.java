/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import manejadorMensaje.ResponseBD;
import modelo.User;

/**
 *
 * @author Christian
 */
public class UserDAO {

    private DB db;

    public UserDAO(DB db) {
        this.db = db;
    }

    public ResponseBD list() {
        Connection conn = null;
        ResponseBD response = new ResponseBD(null, null, null);
        try {
            conn = db.establecerConexion();
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Crear un arreglo para los nombres de los atributos
            String[] attributeNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                attributeNames[i - 1] = rsmd.getColumnName(i);
            }

            ArrayList<String[]> users = new ArrayList<>();
            users.add(attributeNames);  // Añadir los nombres de los atributos como la primera fila

            while (rs.next()) {
                String[] user = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    user[i - 1] = rs.getString(i);
                }
                users.add(user);
            }

            // Convertir la lista de arreglos a un arreglo bidimensional
            String[][] usersArray = new String[users.size()][];
            for (int i = 0; i < users.size(); i++) {
                usersArray[i] = users.get(i);
            }
            response.setTitle("User");
            response.setData(usersArray);
        } catch (SQLException e) {
            response.setError(e.getMessage());
        } finally {
            if (conn != null) {
                db.cerrarConexion();
            }
        }
        return response;
    }

    public String atributosToString(Map<String, Object> atributos) {
        String cadena = "";
        for (Map.Entry<String, Object> entry : atributos.entrySet()) {
            String key = entry.getKey();
            cadena = cadena + key + ",";
        }
        if (cadena.length() > 0) {
            cadena = cadena.substring(0, cadena.length() - 1);
        }

        return cadena;
    }

    public String atributosValorToString(Map<String, Object> atributos) {
        StringBuilder cadena = new StringBuilder();
        for (Map.Entry<String, Object> entry : atributos.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("id")) {
                Object value = entry.getValue();
                if (value instanceof String || value instanceof Date) {
                    cadena.append(key).append("='").append(value).append("',");
                } else if (value instanceof Integer || value instanceof Float || value instanceof Double || value instanceof Boolean) {
                    cadena.append(key).append("=").append(value).append(",");
                }
            }
        }
        if (cadena.length() > 0) {
            cadena.setLength(cadena.length() - 1);  // Elimina la última coma
        }
        return cadena.toString();
    }

    public String[] valoresToString(Map<String, Object> atributos) {
        StringBuilder cadenaKeys = new StringBuilder();
        StringBuilder cadenaValores = new StringBuilder();

        String response[] = new String[2];
        for (Map.Entry<String, Object> entry : atributos.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("id")) {
                Object value = entry.getValue();
                if (value instanceof String || value instanceof Date) {
                    cadenaKeys.append(key).append(",");
                    cadenaValores.append("'").append(value).append("',");
                } else if (value instanceof Integer || value instanceof Float || value instanceof Double || value instanceof Boolean) {
                    cadenaKeys.append(key).append(",");
                    cadenaValores.append(value).append(",");
                }
            }
        }
        if (cadenaValores.length() > 0) {
            cadenaValores.setLength(cadenaValores.length() - 1);  // Elimina la última coma
        }
        if (cadenaKeys.length() > 0) {
            cadenaKeys.setLength(cadenaKeys.length() - 1);  // Elimina la última coma
        }
        response[0] = cadenaKeys.toString();
        response[1] = cadenaValores.toString();
        return response;
    }

    public ResponseBD listAtri(Map<String, Object> atributos) {
        Connection conn = null;
        ResponseBD response = new ResponseBD(null, null, null);
        try {
            conn = db.establecerConexion();
            String atributosString = atributosToString(atributos);
            System.out.println("atributos: " + atributosString);

            String sql = "SELECT " + atributosString + " FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            // Crear un arreglo para los nombres de los atributos
            String[] attributeNames = new String[atributos.size()];
            int i = 0;
            for (String atributo : atributos.keySet()) {
                attributeNames[i++] = atributo;
            }

            ArrayList<String[]> users = new ArrayList<>();
            users.add(attributeNames);  // Añadir los nombres de los atributos como la primera fila

            while (rs.next()) {
                String[] user = new String[atributos.size()];
                i = 0;
                for (String atributo : atributos.keySet()) {
                    user[i++] = rs.getString(atributo);
                }
                users.add(user);
            }

            // Convertir la lista de arreglos a un arreglo bidimensional
            String[][] usersArray = new String[users.size()][];
            for (i = 0; i < users.size(); i++) {
                usersArray[i] = users.get(i);
            }
            response.setTitle("User");
            response.setData(usersArray);
        } catch (SQLException e) {
            response.setError(e.getMessage());
        } finally {
            if (conn != null) {
                db.cerrarConexion();
            }
        }
        return response;
    }

    public ResponseBD create(Map<String, Object> atributos) {
        ResponseBD response = new ResponseBD(null, null, null);
        try {
            Connection conn = db.establecerConexion();
            String atributosString[] = valoresToString(atributos);
            System.out.println("INSERT INTO users (" + atributosString[0] + ") VALUES (" + atributosString[1] + ")");
            String sql = "INSERT INTO users (" + atributosString[0] + ") VALUES (" + atributosString[1] + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                response.setError("Error al insertar el usuario");
            } else {
                response.setTitle("User");
            }
        } catch (SQLException e) {
            response.setError(e.getMessage());
        } finally {
            db.cerrarConexion();
        }
        return response;
    }

    public ResponseBD update(Map<String, Object> atributos) {
        ResponseBD response = new ResponseBD(null, null, null);
        try {
            Connection conn = db.establecerConexion();
            String atributosString = atributosValorToString(atributos);
            String sql = "UPDATE users SET " + atributosString + " WHERE id=" + atributos.get("id") + ";";

            PreparedStatement stmt = conn.prepareStatement(sql);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                response.setError("Error: No se encontró el usuario con el id especificado.");
            } else {
                response.setTitle("User");
            }
            db.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            response.setError("Error: " + e.getMessage());
        }
        return response;
    }

    public ResponseBD show(int id) {
        Connection conn = null;
        ResponseBD response = new ResponseBD(null, null, null);
        try {
            conn = db.establecerConexion();
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Crear un arreglo para los nombres de los atributos
            String[] attributeNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                attributeNames[i - 1] = rsmd.getColumnName(i);
            }

            ArrayList<String[]> users = new ArrayList<>();
            users.add(attributeNames);  // Añadir los nombres de los atributos como la primera fila

            if (rs.next()) {
                String[] user = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    user[i - 1] = rs.getString(i);
                }
                users.add(user);
            } else {
                response.setError("Error: No se encontró al usuario con el id especificado.");
                return response;
            }

            // Convertir la lista de arreglos a un arreglo bidimensional
            String[][] usersArray = new String[users.size()][];
            for (int i = 0; i < users.size(); i++) {
                usersArray[i] = users.get(i);
            }
            response.setTitle("User");
            response.setData(usersArray);
        } catch (SQLException e) {
            response.setError(e.getMessage());
        } finally {
            if (conn != null) {
                db.cerrarConexion();
            }
        }
        return response;
    }

    public ResponseBD delete(int id) {
        Connection conn = null;
        ResponseBD response = new ResponseBD(null, null, null);
        try {
            conn = db.establecerConexion();
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                response.setError("Error: No se encontró el usuario con el id especificado.");
                
            }
        } catch (SQLException e) {
            response.setError(e.getMessage());
        } finally {
            if (conn != null) {
                db.cerrarConexion();
            }
        }
        return response;
    }

}
