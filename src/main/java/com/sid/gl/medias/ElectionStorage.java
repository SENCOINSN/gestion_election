package com.sid.gl.medias;

import com.sid.gl.config.MediaProperties;
import com.sid.gl.exceptions.GestionElectionTechnicalException;
import com.sid.gl.exceptions.ResourceNotFoundException;
import com.sid.gl.users.User;
import com.sid.gl.users.UserMapper;
import com.sid.gl.users.UserRepository;
import com.sid.gl.users.UserResponseDto;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class ElectionStorage {
    private final Path storagePath;
    private final UserRepository userRepository;

    public static final String URI_FILEJPG="/api/v1/users/filejpg/";
    public static final String URI_FILEPNG="/api/v1/users/filepng/";
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 3; //5MB

    public ElectionStorage(MediaProperties mediaProperties, UserRepository userRepository) {
        this.storagePath = Paths.get(mediaProperties.getUploadPath())
                .toAbsolutePath().normalize();
        this.userRepository = userRepository;
        try {
            Files.createDirectories(this.storagePath);
        } catch (Exception ex) {
            log.error("could not creating directory for upload");
            throw new GestionElectionTechnicalException("Could not create the directory where the uploaded files will be stored.", ex);
        }

    }

    public UserResponseDto storeFile(Long userId, @ValidateSize(max = MAX_FILE_SIZE) Optional<MultipartFile> optFile) throws ResourceNotFoundException {
        log.info("storeFile method called for upload");
        if(optFile.isEmpty()){
            log.error("File is empty");
            throw  new ResourceAccessException("Le fichier est vide !!");
        }

        MultipartFile file = optFile.get();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())); //adamaseye.jpg
        validateExtension(fileName);

        try{
            if(fileName.contains("..")) {
                log.error("fileName is invalid path sequence");
                throw new ValidationException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocationSign = this.storagePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocationSign, StandardCopyOption.REPLACE_EXISTING);

            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for upload"));
            String avatarUri = setUriFile(file, fileName, URI_FILEJPG, URI_FILEPNG);
            user.setFileUri(avatarUri);
            User savedUser = userRepository.save(user);
            log.info("File uploaded successfully");
            return UserMapper.toUserResponse(savedUser);
        }catch (IOException e){
            log.error("Failed to upload file", e);
            throw new GestionElectionTechnicalException("Failed to upload file", e);
        }


    }

    //jpg,png,jpeg //jpg,JPG, PNG, JPEG
    private void validateExtension(String fileName){
        if(fileName.contains(".")){
            String extension = fileName.substring(fileName.lastIndexOf(".")); //adamaseye.jpg  //.jpg
            if(!extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".png") && !extension.equalsIgnoreCase(".jpeg")){
                throw new ResourceAccessException("Le fichier n'est pas une image !!");
            }
        }
    }

    // http://localhost:8080/api/v1/users/filejpg/adama.jpg

    public static String setUriFile(MultipartFile file, String fileName,String uriFileJPG,String uriFilePNG){
        String avatarUri;
        if(Objects.equals("image/jpeg",file.getContentType()) || Objects.equals("image/jpg",file.getContentType())) {
            avatarUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(uriFileJPG)
                    .path(fileName)
                    .toUriString();
        }else {
            avatarUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(uriFilePNG)
                    .path(fileName)
                    .toUriString();
        }
        return avatarUri;
    }

    private void validateFileSize(MultipartFile file){
        if(file.getSize() > MAX_FILE_SIZE){
            throw new ResourceAccessException("Le fichier est trop grand !!");
        }
    }


}
