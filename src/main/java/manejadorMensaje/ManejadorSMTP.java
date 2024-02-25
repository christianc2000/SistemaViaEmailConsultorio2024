/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manejadorMensaje;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class ManejadorSMTP {

    private String host;
    private String port;
    private String user;
    private String pass;

    public ManejadorSMTP(String host, String port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    static protected String getMultiline(BufferedReader in) throws IOException {
        String lines = "";
        while (true) {
            String line = in.readLine();
            if (line == null) {
                // Server closed connection
                throw new IOException(" S : Server unawares closed the connection.");
            }
            if (line.charAt(3) == ' ') {
                lines = lines + "\n" + line;
                // No more lines in the server response
                break;
            }
            // Add read line to the list of lines
            lines = lines + "\n" + line;
        }
        return lines;
    }

    public void enviarSMTP(String emisor, String destinatario, String asunto, String cuerpo) {
        try {
            Socket skCliente = new Socket(host, 587);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(skCliente.getInputStream()));
            DataOutputStream salida = new DataOutputStream(skCliente.getOutputStream());

            System.out.println(entrada.readLine());
            String comando = "EHLO " + host + "\r\n";
            salida.writeBytes(comando);
            System.out.println("C: " + comando);
            System.out.println("S: " + getMultiline(entrada));

            comando = "MAIL FROM: " + emisor + "\r\n";
            salida.writeBytes(comando);
            System.out.println("c: " + comando);
            System.out.println("S: " + getMultiline(entrada));

            // comando = "RCPT TO:" + rcptTo + "\r\n";
            comando = "RCPT TO : " + destinatario + " \r\n";
            salida.writeBytes(comando);
            System.out.println("c: " + comando);
            System.out.println("S: " + getMultiline(entrada));

            comando = "DATA\r\n";
            salida.writeBytes(comando);
            System.out.println("c: " + comando);
            System.out.println("S: " + getMultiline(entrada));

            //---String comando="Subject:DEMO X\r\n"+"Probando\n"+"el envio de mensajes\n"+".\r\n";
            comando = "Subject: " + asunto + "\r\n" + cuerpo + "\n" + ".\r\n";
            salida.writeBytes(comando);
            System.out.println("c: " + comando);
            //entrada.readLine();
            System.out.println("S: " + getMultiline(entrada));
            System.out.println("correo grupo:" + destinatario);
            System.out.println("correo origen de destino:" + destinatario);
            System.out.println("mensaje de respuesta enviado");
        } catch (IOException ex) {
            Logger.getLogger(ManejadorSMTP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        Properties prop = new Properties();
        //System.out.println("pass: " + pass);
        prop.put("mail.smtp.host", "smtp.gmail.com");  // Tu servidor SMTP
        prop.put("mail.smtp.socketFactory.port", "465");  // El puerto SMTP
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));  // Tu dirección de correo
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario) // La dirección de correo del destinatario
            );
            message.setSubject(asunto);  // El asunto del correo
            message.setContent(cuerpo, "text/html");  // El cuerpo del correo

            Transport.send(message);

            System.out.println("Correo enviado con éxito.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
