package maja.zmaja.assetsservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Calendar;

public class BaseEntityDto implements Serializable {

    private static final long serialVersionUID = 220848591419234465L;

    private Long id;
    @JsonIgnore
    private Calendar entityCreated;
    @JsonIgnore
    private String uuid;
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseEntityDto other = (BaseEntityDto) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }
}
