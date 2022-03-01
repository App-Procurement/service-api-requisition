package com.synectiks.procurement.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A RolesGroup.
 */
@Entity
@Table(name = "roles_group")
public class RolesGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_group")
    private Boolean group;

    @OneToOne
    @JoinColumn(unique = true)
    private Roles roles;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getGroup() {
        return group;
    }

    public RolesGroup group(Boolean group) {
        this.group = group;
        return this;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public Roles getRoles() {
        return roles;
    }

    public RolesGroup roles(Roles roles) {
        this.roles = roles;
        return this;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolesGroup)) {
            return false;
        }
        return id != null && id.equals(((RolesGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolesGroup{" +
            "id=" + getId() +
            ", group='" + getGroup() + "'" +
            "}";
    }
}
