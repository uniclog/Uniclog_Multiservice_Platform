package local.uniclog.frame_data_access.extensions.service_ekey.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Key Entity from web chat
 * @author erlidione
 */
@Getter
@Setter
@ToString
@Entity(name = "service_eskey")
public class EsKeyEntity {
    @Id
    private String key;
    private LocalDateTime date;

    public EsKeyEntity() {
        this.setDate(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EsKeyEntity that = (EsKeyEntity) o;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return 1996676398;
    }
}