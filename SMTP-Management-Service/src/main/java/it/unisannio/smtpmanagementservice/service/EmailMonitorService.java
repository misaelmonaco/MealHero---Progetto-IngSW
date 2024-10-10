package it.unisannio.smtpmanagementservice.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailMonitorService {

    @Autowired
    private JavaMailSender emailSender;

    private final String username = "mealheroo@gmail.com";
    private final String password = "xmyu dehb xupx eyep";

    @Scheduled(fixedDelay = 15000) // Controlla la casella di posta ogni 15 secondi
    public void checkEmailAndRespond() {
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.ssl.enable", "true");
            properties.put("mail.imap.host", "imap.gmail.com");
            properties.put("mail.imap.port", "993");
            properties.put("mail.store.protocol", "imaps");
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);

            Message[] messages = emailFolder.getMessages();
            for (Message message : messages) {
                respondToEmail(message);
                message.setFlag(Flags.Flag.DELETED, true);
            }

            emailFolder.close(true);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respondToEmail(Message receivedMessage) {
        try {
            String subject = "Casella di posta NO-REPLY";
            String text = "Non inviare email a questo indirizzo perchè è un indirizzo di posta no-reply. \n\nNon è possibile rispondere a questa email!\n\n"
                    + "Se hai bisogno di contattare i developers di MealHero, ecco i loro recapiti:\n"
                    + "Alessandro De Cicco: a.decicco9@studenti.unisannio.it\n"
                    + "Sara Abounnasr: s.abounnasr2@studenti.unisannio.it\n"
                    + "Misael Monaco: m.monaco4@studenti.unisannio.it\n"
                    + "Josef Vita: j.vita@studenti.unisannio.it\n";

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("no-reply@mealhero.com");
            helper.setTo(receivedMessage.getFrom()[0].toString());
            helper.setSubject(subject);
            helper.setText(text);

            emailSender.send(message);
            System.out.println("Email correctly sent! --> Receiver: " + receivedMessage.getFrom()[0].toString() +'\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
