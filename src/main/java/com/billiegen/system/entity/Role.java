package com.billiegen.system.entity;

import com.billiegen.common.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色
 *
 * @author CodePorter
 * @date 2017-09-30
 */
@Entity
@Table(name = "sys_role",
        indexes = {
                @Index(columnList = "roleName", name = "uk_role_name", unique = true)
        }
)
public class Role extends BaseEntity {
    private String roleName;
    private Boolean isSystem;
    private String description;
    private Set<Right> rightSet;
    private Set<Admin> adminSet = new HashSet<>();

    @Override
    @Transient
    public void onSave() {
        super.onSave();
        if (this.isSystem == null) {
            this.isSystem = false;
        }
    }

    @Override
    @Transient
    public void onUpdate() {
        super.onUpdate();
        this.onSave();
    }

    @Column(nullable = false, length = 32)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(nullable = false)
    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany
    @JoinTable(
            name = "sys_role_right",
            joinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_role_right_role")),
            inverseJoinColumns = @JoinColumn(name = "right_id", foreignKey = @ForeignKey(name = "fk_role_right_right"))
    )
    public Set<Right> getRightSet() {
        return rightSet;
    }

    public void setRightSet(Set<Right> rightSet) {
        this.rightSet = rightSet;
    }

    @ManyToMany(mappedBy = "roleSet")
    public Set<Admin> getAdminSet() {
        return adminSet;
    }

    public void setAdminSet(Set<Admin> adminSet) {
        this.adminSet = adminSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        if (!getRoleName().equals(role.getRoleName())) return false;
        if (!isSystem.equals(role.isSystem)) return false;
        return getDescription().equals(role.getDescription());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        int prime = 31;
        result = prime * result + getRoleName().hashCode();
        result = prime * result + isSystem.hashCode();
        result = prime * result + getDescription().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
