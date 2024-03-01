package sample.test.kiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.test.kiosk.spring.client.mail.MailSendClient;
import sample.test.kiosk.spring.domain.history.mail.MailSendHistory;
import sample.test.kiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("메일 전송 테스트")
    void sendMail() {

        // given:
        // @Mock, @InjectMocks 애너테이션으로 대체 가능
//        final MailSendClient mailSendClient = mock(MailSendClient.class);
//        final MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
//        final MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);


        // when:
        final boolean result = mailService.sendMail("fromEmail", "toEmail", "subject", "content");

        // then:
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
        assertThat(result).isTrue();

    }
}