/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manejadorMensaje;

import analizar.Command;
import analizar.Lexer;
import datos.DB;
import datos.UserDAO;
import manejadorMensaje.ResponseBD;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import modelo.User;
import java.sql.Date;

/**
 *
 * @author Christian
 */
public class AnalisisMensajeThread extends Thread {

    MensajeEmisor mensajeEmisor;

    public AnalisisMensajeThread(MensajeEmisor mensajeEmisor) {
        this.mensajeEmisor = mensajeEmisor;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hello World!");
            //String input="START[];";
            //String input="HELP[];";
            //String input = "LIST[users];";
            //String input="LIST[users:id,ci,name];";
            //String input = "INSERT[users:ci=9821736,name=Christian Celso,lastname=Mamani Soliz,birth_date=2000-01-04,genero=M,celular=77376902,tipo=M,residencia_actual=Santa Cruz,sueldo=4000,email=chrstncelso@gmail.com,password=9821736,url_foto=fotografia.png];";
            //String input = "UPDATE[users:id=1,birth_date=2000-01-04,genero=M,celular=77376902,tipo=M,residencia_actual=Santa Cruz,email=chrstncelso@gmail.com,password=9821736,url_foto=fotografia.png];";
            //String input="INSERT[dias:nombre=Sabadingo];";
            // String input="SHOW[dia:id=1];";
            //String input = "DELETE[dias:id=1];";
            Lexer sintaxis = new Lexer();
            Command comando = sintaxis.lex(mensajeEmisor.getMensaje());
            if (comando.error == null) {
                DB db = new DB();
                System.out.println("Acción: " + comando.accion);
                System.out.println("Tabla: " + comando.tabla);
                System.out.println("Atributos: " + comando.atributos);
                switch (comando.accion) {
                    case "START":
                        System.out.println("Mostrar opciones de inicio");
                        break;
                    case "HELP":
                        System.out.println("Mostrar ayuda con los comandos disponibles");
                        break;
                    case "LIST":
                        switch (comando.tabla) {
                            case "users":
                                System.out.println("listar usuarios");
                                UserDAO userDAO = new UserDAO(db);
                                ResponseBD users = userDAO.list();
                                for (int i = 0; i < users.getData().length; i++) {
                                    for (int j = 0; j < users.getData()[i].length; j++) {
                                        System.out.print(users.getData()[i][j] + " ");
                                    }
                                    System.out.println();
                                }
                                break;
                            case "dias":
                                System.out.println("listar dias");
                                break;
                            case "horarios":
                                System.out.println("listar horarios");
                                break;
                            case "turnos":
                                System.out.println("listar turnos");
                                break;
                            case "servicios":
                                System.out.println("listar servicios");
                                break;
                            case "salas":
                                System.out.println("listar salas");
                                break;
                            case "atencions":
                                System.out.println("listar atencions");
                                break;
                            case "citas":
                                System.out.println("listar citas");
                                break;
                            case "fichas":
                                System.out.println("listar fichas");
                                break;
                            case "forma_pagos":
                                System.out.println("listar forma_pagos");
                                break;
                            case "ordens":
                                System.out.println("listar ordens");
                                break;
                            case "historials":
                                System.out.println("listar historials");
                                break;
                            case "dato_medicos":
                                System.out.println("listar dato_medicos");
                                break;
                            case "consultas":
                                System.out.println("listar consultas");
                                break;
                            case "examen_fisicos":
                                System.out.println("listar examen_fisicos");
                                break;
                            case "tratamientos":
                                System.out.println("listar tratamientos");
                                break;
                            default:
                                throw new AssertionError();
                        }

                        break;
                    case "LISTATRI":
                        switch (comando.tabla) {
                            case "users":
                                System.out.println("listar con atributo usuarios");
                                UserDAO userDAO = new UserDAO(db);
                                ResponseBD users = userDAO.listAtri(comando.atributos);
                                for (int i = 0; i < users.getData().length; i++) {
                                    for (int j = 0; j < users.getData()[i].length; j++) {
                                        System.out.print(users.getData()[i][j] + " ");
                                    }
                                    System.out.println();
                                }
                                break;
                            case "dias":
                                System.out.println("listar con atributo dias");

                                break;
                            case "horarios":
                                System.out.println("listar con atributo horarios");
                                break;
                            case "turnos":
                                System.out.println("listar con atributo turnos");
                                break;
                            case "servicios":
                                System.out.println("listar con atributo servicios");
                                break;
                            case "salas":
                                System.out.println("listar con atributo salas");
                                break;
                            case "atencions":
                                System.out.println("listar con atributo atencions");
                                break;
                            case "citas":
                                System.out.println("listar con atributo citas");
                                break;
                            case "fichas":
                                System.out.println("listar con atributo fichas");
                                break;
                            case "forma_pagos":
                                System.out.println("listar con atributo forma_pagos");
                                break;
                            case "ordens":
                                System.out.println("listar con atributo ordens");
                                break;
                            case "historials":
                                System.out.println("listar con atributo historials");
                                break;
                            case "dato_medicos":
                                System.out.println("listar con atributo dato_medicos");
                                break;
                            case "consultas":
                                System.out.println("listar con atributo consultas");
                                break;
                            case "examen_fisicos":
                                System.out.println("listar con atributo examen_fisicos");
                                break;
                            case "tratamientos":
                                System.out.println("listar con atributo tratamientos");
                                break;
                            default:
                                throw new AssertionError();
                        }
                        break;
                    case "INSERT":
                        System.out.println("insertar un nuevo registro de una tabla");
                        switch (comando.tabla) {
                            case "users":
                                UserDAO userDAO = new UserDAO(db);
                                User user = new User();
                                ResponseBD errores = userDAO.create(comando.atributos);
                                System.out.println("luego de crear");
                                System.out.println("errores: " + errores.getError());
                                if (errores.getError() == null) {
                                    System.out.println("Usuario actualizado exitosamente");
                                } else {
                                    System.out.println(errores);
                                }
                                break;
                            case "dias":
                                System.out.println("listar con atributo dias");

                                break;
                            case "horarios":
                                System.out.println("listar con atributo horarios");
                                break;
                            case "turnos":
                                System.out.println("listar con atributo turnos");
                                break;
                            case "servicios":
                                System.out.println("listar con atributo servicios");
                                break;
                            case "salas":
                                System.out.println("listar con atributo salas");
                                break;
                            case "atencions":
                                System.out.println("listar con atributo atencions");
                                break;
                            case "citas":
                                System.out.println("listar con atributo citas");
                                break;
                            case "fichas":
                                System.out.println("listar con atributo fichas");
                                break;
                            case "forma_pagos":
                                System.out.println("listar con atributo forma_pagos");
                                break;
                            case "ordens":
                                System.out.println("listar con atributo ordens");
                                break;
                            case "historials":
                                System.out.println("listar con atributo historials");
                                break;
                            case "dato_medicos":
                                System.out.println("listar con atributo dato_medicos");
                                break;
                            case "consultas":
                                System.out.println("listar con atributo consultas");
                                break;
                            case "examen_fisicos":
                                System.out.println("listar con atributo examen_fisicos");
                                break;
                            case "tratamientos":
                                System.out.println("listar con atributo tratamientos");
                                break;
                            default:
                                throw new AssertionError();
                        }
                        break;
                    case "UPDATE":
                        System.out.println("actualizar un nuevo registro de una tabla");
                        switch (comando.tabla) {
                            case "users":
                                UserDAO userDAO = new UserDAO(db);
                                User user = new User();
                                String errores = userDAO.update(comando.atributos);
                                if (errores.length() == 0) {
                                    System.out.println("Usuario actualizado exitosamente");
                                } else {
                                    System.out.println(errores);
                                }
                                System.out.println("atributos actualizar: " + comando.atributos);
                                break;
                            case "dias":
                                System.out.println("listar con atributo dias");

                                break;
                            case "horarios":
                                System.out.println("listar con atributo horarios");
                                break;
                            case "turnos":
                                System.out.println("listar con atributo turnos");
                                break;
                            case "servicios":
                                System.out.println("listar con atributo servicios");
                                break;
                            case "salas":
                                System.out.println("listar con atributo salas");
                                break;
                            case "atencions":
                                System.out.println("listar con atributo atencions");
                                break;
                            case "citas":
                                System.out.println("listar con atributo citas");
                                break;
                            case "fichas":
                                System.out.println("listar con atributo fichas");
                                break;
                            case "forma_pagos":
                                System.out.println("listar con atributo forma_pagos");
                                break;
                            case "ordens":
                                System.out.println("listar con atributo ordens");
                                break;
                            case "historials":
                                System.out.println("listar con atributo historials");
                                break;
                            case "dato_medicos":
                                System.out.println("listar con atributo dato_medicos");
                                break;
                            case "consultas":
                                System.out.println("listar con atributo consultas");
                                break;
                            case "examen_fisicos":
                                System.out.println("listar con atributo examen_fisicos");
                                break;
                            case "tratamientos":
                                System.out.println("listar con atributo tratamientos");
                                break;
                            default:
                                throw new AssertionError();
                        }
                        break;
                    case "SHOW":
                        System.out.println("mostrar un registro de una tabla");
                        switch (comando.tabla) {
                            case "users":
                                UserDAO userDAO = new UserDAO(db);
                                int id = (Integer) comando.atributos.get("id");
                                ResponseBD response = userDAO.show(id);
                                if (response.getError() == null) {
                                    for (int i = 0; i < response.getData().length; i++) {
                                        for (int j = 0; j < response.getData()[i].length; j++) {
                                            System.out.print(response.getData()[i][j] + " ");
                                        }
                                        System.out.println();
                                    }
                                }

                                break;
                            case "dias":
                                System.out.println("listar con atributo dias");

                                break;
                            case "horarios":
                                System.out.println("listar con atributo horarios");
                                break;
                            case "turnos":
                                System.out.println("listar con atributo turnos");
                                break;
                            case "servicios":
                                System.out.println("listar con atributo servicios");
                                break;
                            case "salas":
                                System.out.println("listar con atributo salas");
                                break;
                            case "atencions":
                                System.out.println("listar con atributo atencions");
                                break;
                            case "citas":
                                System.out.println("listar con atributo citas");
                                break;
                            case "fichas":
                                System.out.println("listar con atributo fichas");
                                break;
                            case "forma_pagos":
                                System.out.println("listar con atributo forma_pagos");
                                break;
                            case "ordens":
                                System.out.println("listar con atributo ordens");
                                break;
                            case "historials":
                                System.out.println("listar con atributo historials");
                                break;
                            case "dato_medicos":
                                System.out.println("listar con atributo dato_medicos");
                                break;
                            case "consultas":
                                System.out.println("listar con atributo consultas");
                                break;
                            case "examen_fisicos":
                                System.out.println("listar con atributo examen_fisicos");
                                break;
                            case "tratamientos":
                                System.out.println("listar con atributo tratamientos");
                                break;
                            default:
                                throw new AssertionError();
                        }
                        break;
                    case "DELETE":
                        System.out.println("eliminar un registro de una tabla");
                        break;
                    default:
                        throw new AssertionError();
                }
            } else {
                System.out.println("Errores\n");
                System.out.println(comando.error);
            }
        } catch (Exception e) {
            // Manejar la excepción...
        } finally {
            // Cerrar recursos...
        }

    }
}
