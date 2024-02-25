/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manejadorMensaje;

import analizar.Command;
import analizar.Lexer;
import datos.DB;
import datos.UserDAO;
import io.github.cdimascio.dotenv.Dotenv;
import manejadorMensaje.ResponseBD;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            Dotenv dotenv = Dotenv.load();
            String host = dotenv.get("MAIL_HOST");
            String userEmail = dotenv.get("GMAIL_USER");
            String passEmail = dotenv.get("GMAIL_PASS");
            String port = "25";
            ManejadorSMTP smtp = new ManejadorSMTP(host, port, userEmail, passEmail);//Para enviar el correo de respuesta
            String htmlContent;
            if (comando.error == null) {
                DB db = new DB();
                ResponseBD response = new ResponseBD(null, null, null);
                System.out.println("Acción: " + comando.accion);
                System.out.println("Tabla: " + comando.tabla);
                System.out.println("Atributos: " + comando.atributos);
                switch (comando.accion) {
                    case "START":
                        System.out.println("Mostrar opciones de inicio");
                        htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\start.html")));
                        //System.out.println("html: " + htmlContent);
                        // smtp.enviarSMTP("grupo01sc@tecnoweb.org.bo", mensajeEmisor.getCorreo(), "Empezar", htmlContent);
                        smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Empezando...", htmlContent);
                        break;
                    case "HELP":
                        System.out.println("Mostrar ayuda con los comandos disponibles");
                        htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\help.html")));
                        smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Ayuda...", htmlContent);
                        break;
                    case "LIST":
                        switch (comando.tabla) {
                            case "users":
                                System.out.println("listar usuarios");
                                UserDAO userDAO = new UserDAO(db);
                                response = userDAO.list();
                                if (response.getError() == null) {
                                    for (int i = 0; i < response.getData().length; i++) {
                                        for (int j = 0; j < response.getData()[i].length; j++) {
                                            System.out.print(response.getData()[i][j] + " ");
                                        }
                                        System.out.println();
                                    }
                                    htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\list.html")));
                                    String insert = "INSERT[users:ci=string,name=string,lastname=string,birth_date=date,genero=string,celular=number,tipo=string,residencia_actual=string,email=string,password=string,url_foto=string,sueldo=number,formacion=string,nit=string,razon_social=string];";
                                    String update = "UPDATE[users:id=number,ci=string,name=string,lastname=string,birth_date=date,genero=string,celular=number,tipo=string,residencia_actual=string,email=string,password=string,url_foto=string,sueldo=number,formacion=string,nit=string,razon_social=string]; COLOQUE TODOS LOS CAMPOS QUE REQUIERA ACTUALIZAR";
                                    String show = "SHOW[users:id=number];";
                                    String delete = "DELETE[users:id=number];";
                                    htmlContent = succesView(htmlContent, response.getTitle(), response.getData(), insert, update, show, delete);
                                    smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Listar", htmlContent);
                                    System.out.println(htmlContent);
                                } else {
                                    System.out.println(response.getError());
                                    comando.error = response.getError();
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
                                comando.error = "La tabla " + comando.tabla + " no es una tabla válida";
                        }
                        break;
                    case "LISTATRI":
                        switch (comando.tabla) {
                            case "users":
                                System.out.println("listar con atributo usuarios");
                                UserDAO userDAO = new UserDAO(db);
                                response = userDAO.listAtri(comando.atributos);
                                if (response.getError() == null) {
                                    for (int i = 0; i < response.getData().length; i++) {
                                        for (int j = 0; j < response.getData()[i].length; j++) {
                                            System.out.print(response.getData()[i][j] + " ");
                                        }
                                        System.out.println();
                                    }
                                    htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\list.html")));
                                    String insert = "INSERT[users:ci=string,name=string,lastname=string,birth_date=date,genero=string,celular=number,tipo=string,residencia_actual=string,email=string,password=string,url_foto=string,sueldo=number,formacion=string,nit=string,razon_social=string];";
                                    String update = "UPDATE[users:id=number,ci=string,name=string,lastname=string,birth_date=date,genero=string,celular=number,tipo=string,residencia_actual=string,email=string,password=string,url_foto=string,sueldo=number,formacion=string,nit=string,razon_social=string]; COLOQUE TODOS LOS CAMPOS QUE REQUIERA ACTUALIZAR";
                                    String show = "SHOW[users:id=number];";
                                    String delete = "DELETE[users:id=number];";
                                    htmlContent = succesView(htmlContent, response.getTitle(), response.getData(), insert, update, show, delete);
                                    smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Listar", htmlContent);
                                    System.out.println(htmlContent);
                                } else {
                                    System.out.println(response.getError());
                                    comando.error = response.getError();
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
                                comando.error = "La tabla " + comando.tabla + " no es una tabla válida";
                        }
                        break;
                    case "INSERT":
                        System.out.println("insertar un nuevo registro de una tabla");
                        switch (comando.tabla) {
                            case "users":
                                UserDAO userDAO = new UserDAO(db);
                                User user = new User();
                                response = userDAO.create(comando.atributos);
                                if (response.getError() == null) {
                                    System.out.println("Usuario registrado exitosamente exitosamente");
                                    htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\succes.html")));
                                    htmlContent = responseView(htmlContent, "$succes", "Usuario registrado con exito");
                                    smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Éxito...", htmlContent);
                                } else {
                                    System.out.println(response.getError());
                                    comando.error = response.getError();
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
                                comando.error = "La tabla " + comando.tabla + " no es una tabla válida";
                        }
                        break;
                    case "UPDATE":
                        System.out.println("actualizar un nuevo registro de una tabla");
                        switch (comando.tabla) {
                            case "users":
                                UserDAO userDAO = new UserDAO(db);
                                User user = new User();
                                response = userDAO.update(comando.atributos);
                                 if (response.getError() == null) {
                                    System.out.println("Usuario actualizado exitosamente exitosamente");
                                    htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\succes.html")));
                                    htmlContent = responseView(htmlContent, "$succes", "Usuario actualizado con exito");
                                    smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Éxito...", htmlContent);
                                } else {
                                    System.out.println(response.getError());
                                    comando.error = response.getError();
                                }
                               // System.out.println("atributos actualizar: " + comando.atributos);
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
                                comando.error = "La tabla " + comando.tabla + " no es una tabla válida";
                        }
                        break;
                    case "SHOW":
                        System.out.println("mostrar un registro de una tabla");
                        switch (comando.tabla) {
                            case "users":
                                UserDAO userDAO = new UserDAO(db);
                                int id = (Integer) comando.atributos.get("id");
                                response = userDAO.show(id);
                                if (response.getError() == null) {
                                    System.out.println("Mostrar usuario");
                                    htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\list.html")));
                                    String insert = "INSERT[users:ci=string,name=string,lastname=string,birth_date=date,genero=string,celular=number,tipo=string,residencia_actual=string,email=string,password=string,url_foto=string,sueldo=number,formacion=string,nit=string,razon_social=string];";
                                    String update = "UPDATE[users:id=number,ci=string,name=string,lastname=string,birth_date=date,genero=string,celular=number,tipo=string,residencia_actual=string,email=string,password=string,url_foto=string,sueldo=number,formacion=string,nit=string,razon_social=string]; COLOQUE TODOS LOS CAMPOS QUE REQUIERA ACTUALIZAR";
                                    String show = "SHOW[users:id=number];";
                                    String delete = "DELETE[users:id=number];";
                                    //htmlContent = succesView(htmlContent, response.getTitle(), response.getData(), insert, update, show, delete);
                                    htmlContent=succesView(htmlContent, response.getTitle(), response.getData(), insert, update, show, delete);
                                    smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Mostrar", htmlContent);
                                } else {
                                    System.out.println(response.getError());
                                    comando.error = response.getError();
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
                                comando.error = "La tabla " + comando.tabla + " no es una tabla válida";
                        }
                        break;
                    case "DELETE":
                        System.out.println("eliminar un registro de una tabla");
                        UserDAO userDAO = new UserDAO(db);
                        int id = (Integer) comando.atributos.get("id");
                        response = userDAO.delete(id);
                        if (response.getError() == null) {
                             System.out.println("Usuario eliminado exitosamente exitosamente");
                                    htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\succes.html")));
                                    htmlContent = responseView(htmlContent, "$succes", "Usuario eliminado con exito");
                                    smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Éxito...", htmlContent);
                        } else {
                            System.out.println(response.getError());
                            comando.error = response.getError();
                        }
                        break;
                    default:
                        comando.error = "La acción " + comando.accion + " no es una acción válida";
                }
            }
            if (comando.error != null) {
                System.out.println(comando.error);
                //System.out.println(comando.error);
                htmlContent = new String(Files.readAllBytes(Paths.get("C:\\Users\\Christian\\Documents\\NetBeansProjects\\SistemViaEmailConsultorio\\src\\main\\java\\presentacion\\error.html")));
                htmlContent = responseView(htmlContent, "$error", comando.error);
                smtp.enviarCorreo(mensajeEmisor.getCorreo(), "Error...", htmlContent);
                // System.out.println(htmlContent);
                //System.out.println("Correo enviado con éxito");
            }
        } catch (Exception e) {
            // Manejar la excepción...
        } finally {
            // Cerrar recursos...
        }

    }

    public String succesView(String htmlContenido, String titulo, String[][] dato, String comandoInsert, String comandoUpdate, String comandoShow, String comandoDelete) {
        String html = "";
        String contenido = "";
        html = htmlContenido.replace("$title", titulo);
        for (int i = 0; i < dato.length; i++) {
            contenido = contenido + "\n<tr>";
            for (int j = 0; j < dato[i].length; j++) {
                if (i == 0) {
                    contenido = contenido + "\n<th>";
                    contenido = contenido + dato[i][j];
                    contenido = contenido + "</th>";
                } else {
                    contenido = contenido + "\n<td>";
                    contenido = contenido + dato[i][j];
                    contenido = contenido + "</td>";
                }
            }
            contenido = contenido + "\n</tr>";
        }
        html = html.replace("$insert", comandoInsert);
        html = html.replace("$update", comandoUpdate);
        html = html.replace("$show", comandoShow);
        html = html.replace("$delete", comandoDelete);
        html = html.replace("$content", contenido);
        return html;
    }

    public String responseView(String htmlContenido, String comando, String error) {
        String html = htmlContenido.replace(comando, error);
        return html;
    }
}
