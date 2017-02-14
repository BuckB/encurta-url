package br.com.bemobi.encurtaurl.service;

import br.com.bemobi.encurtaurl.domain.Link;

import java.util.List;

/**
 * Service Interface for managing Link.
 */
public interface LinkService {

    /**
     * Save a link.
     *
     * @param link the entity to save
     * @return the persisted entity
     */
    Link save(Link link) throws Exception;

    /**
     *  Get all the links.
     *
     *  @return the list of entities
     */
    List<Link> findAll();

    /**
     *  Get the "id" link.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Link findOne(Long id);

    /**
     *  Delete the "id" link.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
