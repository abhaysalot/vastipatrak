package com.aviyan.vastipatrak.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String from, String to, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        send(from, Arrays.asList(to), null, null, subject, content, Collections.emptyList(), Collections.emptyMap());
    }

    public void send(String from, List<String> to, List<String> cc, List<String> bcc,
                     String subject, String content, List<MultipartFile> multipartFiles, Map<String, byte[]> attachmentsMap) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setFrom(new InternetAddress(from, from, from));
        helper.setTo(to.toArray(new String[0]));
        if(Objects.nonNull(cc) && cc.size() > 0){
            helper.setCc(cc.toArray(new String[0]));
        }
        if(Objects.nonNull(bcc) && bcc.size() > 0){
            helper.setBcc(bcc.toArray(new String[0]));
        }
        helper.setSubject(subject);

        BodyPart messageBodyPart = new MimeBodyPart();
        content = content.replaceAll("\n", "<br>");
        messageBodyPart.setContent(content, "text/html");

        List<File> attachments = new ArrayList<>();
        MimeBodyPart attachmentPart = new MimeBodyPart();
        //Attach files
        if(Objects.nonNull(multipartFiles)){
            multipartFiles.forEach(multipartFile -> {
                File attachment = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                try (FileOutputStream fos = new FileOutputStream(attachment)){
                    fos.write(multipartFile.getBytes());
                    attachmentPart.attachFile(attachment);
                    attachments.add(attachment);
                } catch (IOException | MessagingException e) {
                    log.error("There is an error while attaching file to email. The message is {}", e.getMessage(), e);
                }
            });
        }

        if(Objects.nonNull(attachmentsMap)){
            attachmentsMap.forEach((filename, attachmentFile) -> {
                File attachment = new File(Objects.requireNonNull(filename));
                try (FileOutputStream fos = new FileOutputStream(attachment)){
                    fos.write(attachmentFile);
                    attachmentPart.attachFile(attachment);
                    attachments.add(attachment);
                } catch (IOException | MessagingException e) {
                    log.error("There is an error while attaching file to email. The message is {}", e.getMessage(), e);
                }
            });
        }

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        if((Objects.nonNull(multipartFiles) && multipartFiles.size() > 0) || (Objects.nonNull(attachmentsMap) && attachmentsMap.size() > 0)){
            multipart.addBodyPart(attachmentPart);
        }
        mimeMessage.setContent(multipart);

        javaMailSender.send(mimeMessage);

        //Remove the attachments from file system
        attachments.forEach(File::delete);
    }
}
