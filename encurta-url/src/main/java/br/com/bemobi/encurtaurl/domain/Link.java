package br.com.bemobi.encurtaurl.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Link.
 */
@Entity
@Table(name = "link")
@NamedQueries({
    @NamedQuery(name = "Link.findByAlias", query = "SELECT l FROM Link as l WHERE l.alias = :alias")
})
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 11, max = 100)
    @Pattern(regexp = "(^(https?:\\/\\/)?(www\\.)?([a-zA-Z0-9]+\\.)([a-zA-Z]{2,3})(\\.[a-zA-Z]{2,3})?$)")
    @Column(name = "url", nullable = false)
    private String url;

//    @NotNull
    @Size(min = 3, max = 10)
    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "time_taken")
    private Long time_taken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Link url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public Link alias(String alias) {
        this.alias = alias;
        return this;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Long getTime_taken() {
        return time_taken;
    }

    public Link time_taken(Long time_taken) {
        this.time_taken = time_taken;
        return this;
    }

    public void setTime_taken(Long time_taken) {
        this.time_taken = time_taken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        if (link.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, link.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Link{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", alias='" + alias + "'" +
            ", time_taken='" + time_taken + "'" +
            '}';
    }
}
