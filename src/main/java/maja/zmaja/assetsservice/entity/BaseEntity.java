package maja.zmaja.assetsservice.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTITY_CREATED")
    private Calendar entityCreated;

    @Column(name="UUID", length=36)
    private String uuid;

    public BaseEntity() {
        this.setEntityCreated(Calendar.getInstance());
        this.uuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getEntityCreated() {
        return entityCreated;
    }

    public void setEntityCreated(Calendar entityCreated) {
        this.entityCreated = entityCreated;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
