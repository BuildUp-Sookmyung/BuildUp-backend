package buildup.server.member.service;

import buildup.server.member.domain.Profile;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

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
}
