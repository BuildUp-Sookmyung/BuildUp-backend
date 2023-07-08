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

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/png");
            objectMetadata.setContentLength(file.getSize());


            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                fileUrls.add(amazonS3Client.getUrl(bucket, key).toString());
            } catch (IOException ex) {
                log.error("이미지 업로드 IOException");
                throw new S3Exception(S3ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        if(fileUrls.isEmpty()){
            for(int i = 0;i<3;i++){
                fileUrls.add(null);
            }
        }else if(fileUrls.size() < 3){
            for(int i = 0;i<4-fileUrls.size();i++){
                fileUrls.add(null);
            }

        }
        return fileUrls;
    }

    @Transactional
    public String uploadOneRecord(MultipartFile multipartFile) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String storeFileName = UUID.randomUUID() + "." + ext;
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
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ex) {
            log.error("이미지 업로드 IOException");
            throw new S3Exception(S3ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }


    private Member findCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName()).get();
        return member;
    }



}
