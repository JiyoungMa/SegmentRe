package segment.user;

import javax.persistence.*;

@Entity
@Table(name = "group_permission")
public class GroupPermission{
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="group_id")
    private Group group;

    @ManyToOne(optional = false)
    @JoinColumn(name="permission_id")
    private Permission permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
