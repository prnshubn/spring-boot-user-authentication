package com.wipro.newsapp.user.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long role_id;

    @Column(name = "role_name")
    String roleName;

    @Column(name = "role_description")
    String roleDescription;

    public Role() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Role(long role_id, String roleName, String roleDescription) {
        super();
        this.role_id = role_id;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

}
