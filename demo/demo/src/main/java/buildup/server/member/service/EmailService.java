package buildup.server.member.service;

import buildup.server.auth.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisUtil redisUtil;

    @Transactional
    public boolean verifyAuthNum(String email, String input) {
        String data = redisUtil.getData(email);
        if (data == null)
            return false;
        return data.equals(input);
    }

    @Transactional
    public void sendEmail(String toEmail) throws MessagingException {

        if (redisUtil.existData(toEmail))
            redisUtil.deleteData(toEmail);

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);

        //실제 메일 전송
        emailSender.send(emailForm);
        log.info("이메일 전송 성공");

    }

    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }

    private MimeMessage createEmailForm(String toEmail) throws MessagingException {

        String code = createCode();//인증 코드 생성
        String setFrom = "buildupbackend0204@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "BuildUp 회원가입 인증 번호"; //제목

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(code), "utf-8", "html");

        // 인증 코드 레디스에 저장 유효시간 5분
        redisUtil.setDataExpire(toEmail, code, 60 * 5L);

        return message;
    }

    //타임리프를 이용한 context 설정
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context); //mail.html
    }

}
