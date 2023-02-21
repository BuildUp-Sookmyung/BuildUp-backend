package buildup.server.member.service;

import buildup.server.auth.exception.AuthException;
import buildup.server.common.RedisUtil;
import buildup.server.member.domain.Code;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.CodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Optional;
import java.util.Random;

import static buildup.server.member.exception.MemberErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final CodeRepository codeRepository;
    private final RedisUtil redisUtil;

    @Transactional
    public Long verifyCodeByRdb(String email, String input) {
        Code data = codeRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_EMAIL_AUTH_FAILED));
        if (data.getCode() == null)
            return null;
        if (data.getCode().equals(input)) {
            data.setAuthYn("Y");
            return data.getId();
        }
        return null;
    }

    @Transactional
    public boolean verifyCodeByRedis(String email, String code) {
        String data = redisUtil.getData(email);
        if (data == null) { // email이 존재하지 않으면, 유효 기간 만료이거나 코드 잘못 입력
            throw new MemberException(MEMBER_NOT_AUTHENTICATED);
        }
        // 해당 email로 user를 꺼낸다.
        return data.equals(code);
    }

    @Transactional
    public boolean deleteCode(Long codeId) {
        Code code = codeRepository.findById(codeId).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        codeRepository.delete(code);
        return true;
    }

    @Transactional
    public void sendEmail(String name, String toEmail) throws MessagingException {

         Optional<Code> optionalCode = codeRepository.findByEmail(toEmail);
         if (optionalCode.isPresent())
            codeRepository.delete(optionalCode.get());

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(name, toEmail);

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

    private MimeMessage createEmailForm(String name, String toEmail) throws MessagingException {

        String code = createCode();//인증 코드 생성
        String setFrom = "buildupbackend0204@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "BuildUp 회원가입 인증 번호"; //제목

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(name, code), "utf-8", "html");

        codeRepository.save(new Code(name, toEmail, code));

        return message;
    }

    //타임리프를 이용한 context 설정
    private String setContext(String name, String code) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("name", name);
        return templateEngine.process("mail", context); //mail.html
    }

}
