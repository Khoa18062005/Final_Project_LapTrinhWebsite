package viettech.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Column(name = "access_level", nullable = false)
    private String accessLevel;

    @Column(name = "department", nullable = false)
    private String department;

    /* =========================
       CONSTRUCTORS
       ========================= */

    // Constructor mặc định (BẮT BUỘC cho JPA)
    public Admin() {
        super();

        this.roleID = 1;
        this.accessLevel = "";
        this.department = "";
    }

    // Constructor dùng khi tạo admin
    public Admin(String firstName,
                 String lastName,
                 String username,
                 String password,
                 String email,
                 String phone,
                 String avatar,
                 String gender,
                 String accessLevel,
                 String department) {

        super(firstName, lastName, username, password, email, phone, avatar, gender);

        this.accessLevel = accessLevel != null ? accessLevel : "";
        this.department = department != null ? department : "";
    }

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel != null ? accessLevel : "";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department != null ? department : "";
    }
}
