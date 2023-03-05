package buildup.server.member.service;

import buildup.server.activity.domain.Activity;
import buildup.server.common.exception.S3ErrorCode;
import buildup.server.common.exception.S3Exception;
import buildup.server.member.domain.Member;
import buildup.server.member.domain.Profile;
import buildup.server.member.repository.MemberRepository;
import buildup.server.record.exception.RecordErrorCode;
import buildup.server.record.exception.RecordException;
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
    public String uploadProfile(Long memberId, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "profile" + memberId.toString() + "." + ext;
        String key = "profiles/" + storeFileName;

        return putObject(multipartFile, key);
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
    public String uploadActivity(Long activityId, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "activity" + activityId.toString() + "." + ext;
        String key = "activities/" + storeFileName;

        return putObject(multipartFile, key);
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
    @Transactional
    public List<String> uploadRecord(List<MultipartFile> multipartFiles) {

        if (multipartFiles.size() > 3) { throw new RecordException(RecordErrorCode.FILE_COUNT_EXCEED);} // 파일 업로드 갯수 3개 이하

        List<String> fileUrls = new ArrayList<>();


        for (MultipartFile file : multipartFiles) {

            if (file.isEmpty()) { break;}

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String storeFileName = UUID.randomUUID() + "." + ext;
            String key = "records/" + storeFileName;

            log.info("original file content-type: {}", file.getContentType());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());


            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                log.info("content-type: {}", amazonS3Client.getObject(bucket, key).getObjectMetadata().getContentType());
                fileUrls.add(amazonS3Client.getUrl(bucket, key).toString());
            } catch (IOException ex) {
                log.error("이미지 업로드 IOExcpetion");
                throw new S3Exception(S3ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        return fileUrls;
    }

    @Transactional
    public String uploadOneRecord(MultipartFile multipartFile) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = "record/" + originalFilename;
        String key = "records/" + storeFileName;

        return putObject(multipartFile, key);
    }

    @Transactional
    public void deleteOneRecord(String key) {
        try{
            amazonS3Client.deleteObject(bucket, key.substring(59));
        } catch (Exception ex) {
            log.error("S3 Delete Error: {}", ex.getMessage());
            throw new RuntimeException();
        }
    }

    private String putObject(MultipartFile multipartFile, String key) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        log.info("original file content-type: {}", multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("content-type: {}", amazonS3Client.getObject(bucket, key).getObjectMetadata().getContentType());
        } catch (IOException ex) {
            log.error("이미지 업로드 IOExcpetion");
            throw new S3Exception(S3ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        return amazonS3Client.getUrl(bucket, key).toString();
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
