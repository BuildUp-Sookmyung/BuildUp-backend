package buildup.server.member.service;

import buildup.server.member.domain.Member;
import buildup.server.member.dto.NewLoginRequest;
import buildup.server.member.exception.MemberErrorCode;
import buildup.server.member.exception.MemberException;
import buildup.server.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    private final MemberRepository memberRepository;

    private String code;

    @Transactional
    public String sendEmail(String name, String toEmail) throws MessagingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(name, toEmail);

        //실제 메일 전송
        emailSender.send(emailForm);
        log.info("이메일 전송 성공");

        return this.code;
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

        code = createCode();//인증 코드 생성
        String setFrom = "buildupbackend0204@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "BuildUp 인증 번호 메일"; //제목

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(name, code), "utf-8", "html");
        return message;
    }

    //타임리프를 이용한 context 설정
    private String setContext(String name, String code) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("name", name);
        return templateEngine.process("mail3", context); //mail3.html
    }

    @Transactional
    public String[] findIdAndDate(String email) throws MemberException {

        List<Member> findMemberIDList = memberRepository.findAllByEmail(email);

        if(!findMemberIDList.isEmpty()){
            for(Member member : findMemberIDList){
                if(member.getProvider().toString() == "LOCAL"){
                    String memberUsername = member.getUsername();
                    String memberCreated = member.getCreatedAt().toString().substring(0, 10) + " 가입";
                    String[] result = {memberUsername, memberCreated};
                    return result;
                }
            }
            throw new MemberException(MemberErrorCode.ACCOUNT_IN_SOCIAL);
        }else{
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);    // 등록된 id 없을때
        }

    }

    @Transactional
    public void updatePw(NewLoginRequest requestDto) {
        List<Member> findMemberIDList = memberRepository.findAllByEmail(requestDto.getEmail());

        if(!findMemberIDList.isEmpty()){
            for(Member member : findMemberIDList){
                if(member.getProvider().toString() == "LOCAL"){
                    member.modifyPw(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(requestDto.getPassword()));
                }
            }
        }else{
            throw new MemberException(MemberErrorCode.MEMBER_PW_UPDATE_FAILED);
        }
    }




}
