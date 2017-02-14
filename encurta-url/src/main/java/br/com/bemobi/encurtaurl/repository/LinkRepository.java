package br.com.bemobi.encurtaurl.repository;

import br.com.bemobi.encurtaurl.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Link entity.
 */

public interface LinkRepository extends JpaRepository<Link,Long> {

   public Link findByAlias (String alias);
}
