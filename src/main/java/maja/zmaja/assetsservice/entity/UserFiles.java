package maja.zmaja.assetsservice.entity;

import javax.persistence.*;

@Table(name="user_files")
@Entity
public class UserFiles extends BaseEntity{
    
    @Column(name="file_name")
    private String fileName;

    @Column(name="file_url")
    private String fileUrl;

    @Column(name="is_public")
    private Boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public UserFiles() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
