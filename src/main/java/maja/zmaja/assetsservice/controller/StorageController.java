package maja.zmaja.assetsservice.controller;

import maja.zmaja.assetsservice.dao.RoleRepository;
import maja.zmaja.assetsservice.dao.UserFilesRepository;
import maja.zmaja.assetsservice.dao.UserRepository;
import maja.zmaja.assetsservice.entity.Role;
import maja.zmaja.assetsservice.entity.User;
import maja.zmaja.assetsservice.entity.UserFiles;
import maja.zmaja.assetsservice.entity.response.JwtResponse;
import maja.zmaja.assetsservice.enums.UserRole;
import maja.zmaja.assetsservice.storage.config.AmazonClient;
import maja.zmaja.assetsservice.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.websocket.server.PathParam;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;


@RestController
@RequestMapping("/api")
public class StorageController {
    private static final Logger logger = LoggerFactory.getLogger(StorageController.class);

    @Autowired
    private StorageService storageService;
    
    @Autowired
    private UserFilesRepository userFilesRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.endpointUrl}")
    private String endpointUrl;

    /**
     *
     * @param file
     * @param fileName
     * Method upload files in bucket and store user, keyFile and fileUrl in database
     *
     **/
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @Transactional
    @PostMapping("/storage/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "fileName") String fileName) {
       try {
           String username = SecurityContextHolder.getContext().getAuthentication().getName();
           User user = userRepository.findByUsername(username);
           if (user == null)
               return ResponseEntity.badRequest().body("Error, some issue with user!");
           String name = storageService.uploadFile(fileName, file);
           if ((!name.isEmpty()) && name != null) {
               UserFiles userFiles = new UserFiles();
               userFiles.setUser(user);
               userFiles.setFileName(name);
               userFiles.setFileUrl(endpointUrl + name);
               userFiles.setPublic(false);
               userFiles = userFilesRepository.save(userFiles);
               return ResponseEntity.ok().body(userFiles);
           }
           return ResponseEntity.badRequest().body("Error");
       }catch (Exception e) {
           return ResponseEntity.badRequest().body("Error " + e.getMessage());
       }
    }

    /**
     *
     * @param linkFile public file link
     *
     **/
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/storage/downoladPublicFile")
    public ResponseEntity<?> downloadPublicFile(@RequestPart(value = "linkFile") String linkFile) {
        try {
            ByteArrayOutputStream outputStream;
            String fileName = linkFile.substring(endpointUrl.length()); // keyFile
            if (storageService.isPublic(fileName)) {
                outputStream = storageService.downloadFile(fileName);
                return ResponseEntity.ok()
                        .contentType(contentType(fileName))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(outputStream.toByteArray());
            } else
                return ResponseEntity.ok().body("Access Denied!");
        }catch (StringIndexOutOfBoundsException s) {
            ResponseEntity.badRequest().body("Some issue with link");
        }catch (Exception e) {
            ResponseEntity.badRequest().body("Error" + e.getMessage());
        }
        return  null;
    }

    /**
     *
     * @param fileName
     *
     **/
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/storage/downoladFile")
    public ResponseEntity<?> downloadFile(@RequestPart(value = "fileName") String fileName) {
        try {
            ByteArrayOutputStream outputStream = storageService.downloadFile(fileName);
            return ResponseEntity.ok()
                    .contentType(contentType(fileName))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(outputStream.toByteArray());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error " + e.getMessage());
        }
    }

    /**
     *
     * For user with role USER takes user's files (keyFile/file name) from databese and forward to method retrieve
     * user's list from S3. If user has ADMIN role he retrieve all files from bucket.
     **/
    
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/storage/userFiles")
    public ResponseEntity<?> getUserFiles() {
        try {
            List<String> result = new ArrayList<>();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
            List<UserFiles> userFilesList = userFilesRepository.findUserFilesByUser(user);
            Set<Role> roles = user.getRoles();
            Role adminRole = roleRepository.findByRole(UserRole.ADMIN);
            Set<String> userFilesSet = new HashSet<String>();
            if (!roles.contains(adminRole)) {
                for (UserFiles uf : userFilesList) {
                    userFilesSet.add(uf.getFileName());
                }
                result = storageService.listUserFiles(userFilesSet);
            } else
                result = storageService.listFiles();
            return ResponseEntity.ok().body(result);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error "  + e.getMessage());
        }
    }

    private MediaType contentType(String filename) {
        
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
    
}
