package hellojpql;

import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

//    @BatchSize(size = 100)
    @OneToMany(mappedBy = "team")
    private List<Member> memberList = new ArrayList<>();
}
