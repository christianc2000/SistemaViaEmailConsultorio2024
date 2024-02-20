/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manejadorMensaje;

import java.io.IOException;
import javax.mail.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.MimeMultipart;

public class ManejadorPOP3 {

    private String host;
    private String user;
    private String password;
    private String port;
    private int messageCount;

    public ManejadorPOP3(String host, String user, String password, String port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        this.messageCount = 0;

    }

    public void startCheckingMail() {
        while (true) {
            checkMail();
            try {
                // Espera 5 minutos antes de revisar los mensajes de nuevo
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkMail() {
        try {
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.user", user);
            properties.put("mail.pop3.password", password);
            properties.put("mail.pop3.port", port);
            properties.put("mail.pop3.auth", "true");

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3");
            store.connect(host, user, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            int newMessageCount = emailFolder.getMessageCount();
            if (newMessageCount > messageCount) {
                messageCount = newMessageCount;
                Message[] messages = emailFolder.getMessages();
                Message newMessage = messages[messages.length - 1];
                // Aquí puedes acceder al contenido del nuevo mensaje
                String mensaje = getTextFromMessage(newMessage);
                // Obtener el remitente del mensaje
                String remitente = newMessage.getFrom()[0].toString();
                String datosRemitente[] = informacionRemitente(remitente);

                MensajeEmisor mensajeEmisor = new MensajeEmisor(datosRemitente[0], datosRemitente[1], mensaje);
                Thread thread = new Thread(new AnalisisMensajeThread(mensajeEmisor));
                thread.start();
                System.out.println("nombre: " + mensajeEmisor.getNombre());
                System.out.println("correo: " + mensajeEmisor.getCorreo());
                System.out.println("mensaje: " + mensajeEmisor.getMensaje());
                // Lanzar un nuevo hilo con el contenido del mensaje
            }

            emailFolder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ManejadorPOP3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ManejadorPOP3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManejadorPOP3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/*")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
         result = result.replace("\n", "").replace("\r", "");  // Elimina todos los saltos de línea
        return result.trim();
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public String[] informacionRemitente(String remitente) {
        String r[] = new String[2];
        int pi = remitente.indexOf("<");
        int pf = remitente.indexOf(">");
        String remitenteNombre = remitente.substring(0, pi - 1);
        String remitenteEmail = remitente.substring(pi + 1, pf);

        r[0] = remitenteNombre;
        r[1] = remitenteEmail;
        return r;
    }
}
