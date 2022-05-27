package segment.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="groups")
public class Group {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "group")
    private List<GroupPermission> permissions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<GroupPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<GroupPermission> permissions) {
        this.permissions = permissions;
    }

    public List<GrantedAuthority> getAuthorities(){
        return permissions.stream()
                .map(gp -> new SimpleGrantedAuthority(gp.getPermission().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorities =" + permissions +
                '}';
    }
}
