package Server.library.models;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @Column(nullable = false, unique = true)
    private String role_name;

    // Getters and setters
    public Long getRoleId() { return role_id; }
    public void setRoleId(Long roleId) { this.role_id = roleId; }
    public String getRoleName() { return role_name; }
    public void setRoleName(String roleName) { this.role_name = roleName; }
}