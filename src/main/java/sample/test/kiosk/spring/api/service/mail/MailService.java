package sample.test.kiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.test.kiosk.spring.client.mail.MailSendClient;
import sample.test.kiosk.spring.domain.history.mail.MailSendHistory;
import sample.test.kiosk.spring.domain.history.mail.MailSendHistoryRepository;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
        boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);

        if(result) {
            final MailSendHistory history = mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build());
            mailSendHistoryRepository.save(history);
            return true;
        }
        return false;
    }
}
