/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Christian
 */
public class DB {

    Dotenv dotenv = Dotenv.load();

    Connection conectar = null;
    String servidor = dotenv.get("DB_HOST");
    String usuario = dotenv.get("DB_USER");
    String contraseña = dotenv.get("DB_PASS");
    String db = dotenv.get("DB_DATABASE");
    String cadena = "jdbc:postgresql://" + servidor + "/" + db;

    public DB(){};

    public Connection establecerConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            conectar = DriverManager.getConnection(cadena, usuario, contraseña);
            System.out.println("Se estableció la conexión a la base de datos correctamente");
        } catch (Exception e) {
            System.out.println("Error al conectar a la Base de datos: " + e.toString());
        }
        return conectar;
    }

    public void cerrarConexion() {
        try {
            this.conectar.close();
            System.out.println("[ Servidor de BD: desconectado ]");
        } catch (Exception e) {
            System.out.println("Error en cerrar la conexion a la base de datos" + e.toString());
        }
    }
}
