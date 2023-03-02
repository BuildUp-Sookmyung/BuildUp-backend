package buildup.server.member.service;

import buildup.server.activity.domain.Activity;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.repository.MemberRepository;
import buildup.server.record.RecordErrorCode;
import buildup.server.record.RecordException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final MemberRepository memberRepository;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String uploadProfile(Profile profile, Long memberId, MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "profile" + memberId.toString() + "." + ext;
        String key = "profiles/" + storeFileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ex) {
            log.error("이미지 업로드 IOExcpetion");
            throw new RuntimeException();
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Transactional
    public void deleteProfile(String key) {
        try{
            amazonS3Client.deleteObject(bucket, key.substring(59));
        } catch (Exception ex) {
            log.error("S3 Delete Error: {}", ex.getMessage());
            throw new RuntimeException();
        }
    }

    @Transactional
    public String uploadActivity(Activity activity, Long memberId, MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "activity" + memberId.toString() + "." + ext;
        String key = "acitivties/" + storeFileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ex) {
            log.error("이미지 업로드 IOExcpetion");
            throw new RuntimeException();
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Transactional
    public void deleteActivity(String key) {
        try{
            amazonS3Client.deleteObject(bucket, key.substring(59));
        } catch (Exception ex) {
            log.error("S3 Delete Error: {}", ex.getMessage());
            throw new RuntimeException();
        }
    }

    public List<String> uploadRecord(List<MultipartFile> multipartFiles) {

        Member member = findCurrentMember();

        List<String> fileUrls = new ArrayList<>();

        // 파일 업로드 갯수 3개 이하
        for (MultipartFile file : multipartFiles) {
            if (fileUrls.size() > 3) {
                throw new RecordException(RecordErrorCode.FILE_COUNT_EXCEED);
            }

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String storeFileName = "activity" + member.getId().toString() + "." + ext;
            String key = "acitivties/" + storeFileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());


            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                fileUrls.add(amazonS3Client.getUrl(bucket,key).toString());
            } catch (IOException ex) {
                log.error("이미지 업로드 IOExcpetion");
                throw new RecordException(RecordErrorCode.IMAGE_UPLOAD_ERROR);
            }
        }

        return fileUrls;
    }

    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).get();
        return member;
    }

//  TODO :입니다

//    private String createFileName(String fileName) {
//        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
//    }//이미지 파일명 중복 안되게
//
//    private String getFileExtension(String fileName) {
//        if (fileName.length() == 0) {
//            throw new RecordException(RecordErrorCode.WRONG_INPUT_IMAGE);
//        }
//        ArrayList<String> fileValidate = new ArrayList<>();
//        fileValidate.add("");
//        fileValidate.add(".jpg");
//        fileValidate.add(".jpeg");
//        fileValidate.add(".png");
//        fileValidate.add(".JPG");
//        fileValidate.add(".JPEG");
//        fileValidate.add(".PNG");
//        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
//        if (!fileValidate.contains(idxFileName)) {
//            throw new RecordException(RecordErrorCode.WRONG_IMAGE_FORMAT);
//        }
//        return fileName.substring(fileName.lastIndexOf("."));
//    } // TODO : 파일 유효성 검사 (해줘야 한다는데 해야할지 말지 모르겠음)

}
