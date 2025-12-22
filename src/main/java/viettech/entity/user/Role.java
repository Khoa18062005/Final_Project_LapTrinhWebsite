package viettech.entity.user;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name", length = 100, nullable = false, unique = true)
    private String roleName;

    @Column(name = "description", length = 500)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission", length = 200)
    private List<String> permissions;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Role() {
        this.roleName = "";
        this.description = "";
        this.permissions = new ArrayList<>();
    }

    // Constructor đầy đủ tham số (KHÔNG có roleId)
    public Role(String roleName,
                String description,
                List<String> permissions) {

        this.roleName = roleName != null ? roleName : "";
        this.description = description != null ? description : "";
        this.permissions = permissions != null ? new ArrayList<>(permissions) : new ArrayList<>();
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName != null ? roleName : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public List<String> getPermissions() {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions != null ? new ArrayList<>(permissions) : new ArrayList<>();
    }
}