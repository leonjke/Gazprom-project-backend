package net.gazpromkitchen.server.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "topics")
@Data
public class Topic extends BaseEntity {

    private User author;

    private String serviceName;

    private String shortName;

    private String description;

    private Integer quantityUsers;

    private String destination;

    private List<Comment> comments;
}
