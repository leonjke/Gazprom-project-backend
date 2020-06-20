package net.gazpromkitchen.server.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE)
    Date created;

    @LastModifiedDate
    @Column(name = "updated", insertable = false)
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public BaseEntity() {
    }


}
