package br.com.bemobi.encurtaurl.web.rest;

import br.com.bemobi.encurtaurl.domain.Link;
import br.com.bemobi.encurtaurl.repository.LinkRepository;
import br.com.bemobi.encurtaurl.service.LinkService;
import br.com.bemobi.encurtaurl.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Link.
 */
@RestController
@RequestMapping("/api")
public class LinkResource {

    private final Logger log = LoggerFactory.getLogger(LinkResource.class);

    @Inject
    private LinkService linkService;

    @Inject
    private LinkRepository linkRepository;

    /**
     * POST  /links : Create a new link.
     *
     * @param link the link to create
     * @return the ResponseEntity with status 201 (Created) and with body the new link, or with status 400 (Bad Request) if the link has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/links")
    @Timed
    public ResponseEntity<Link> createLink(@RequestBody Link link) throws URISyntaxException, Exception {
        log.debug("REST request to save Link : {}", link);
        if (link.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("link", "idexists", "Um novo link não pode ter um id existente")).body(null);
        }
        Link result = linkService.save(link);
//        if (result != null) {
            return ResponseEntity.created(new URI("/api/links/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("link", result.getId().toString()))
                .body(result);
//        } else {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("link", "aliasexistsurlinvalid", ""));
//        }
    }

    /**
     * PUT  /links : Updates an existing link.
     *
     * @param link the link to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated link,
     * or with status 400 (Bad Request) if the link is not valid,
     * or with status 500 (Internal Server Error) if the link couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/links")
    @Timed
    public ResponseEntity<Link> updateLink(@Valid @RequestBody Link link) throws Exception {
        log.debug("REST request to update Link : {}", link);
        if (link.getId() == null) {
            return createLink(link);
        }
        Link result = linkService.save(link);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("link", link.getId().toString()))
            .body(result);
    }

    /**
     * GET  /links : get all the links.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of links in body
     */
    @GetMapping("/links")
    @Timed
    public List<Link> getAllLinks() {
        log.debug("REST request to get all Links");
        return linkService.findAll();
    }

    /**
     * GET  /links/:id : get the "id" link.
     *
     * @param id the id of the link to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the link, or with status 404 (Not Found)
     */
    @GetMapping("/links/{id}")
    @Timed
    public ResponseEntity<Link> getLink(@PathVariable Long id) {
        log.debug("REST request to get Link : {}", id);
        Link link = linkService.findOne(id);
        return Optional.ofNullable(link)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /links/:alias : get the "alias" link.
     *
     * @param alias the alias of the link to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the link, or with status 404 (Not Found)
     */
    @GetMapping("/links/{alias}")
    @Timed
    public ResponseEntity<Link> getLink(@PathVariable String alias) {
        log.debug("REST request to get Link : {}", alias);
        Link link = linkRepository.findByAlias(alias);
        return Optional.ofNullable(link)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /links/:id : delete the "id" link.
     *
     * @param id the id of the link to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/links/{id}")
    @Timed
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        log.debug("REST request to delete Link : {}", id);
        linkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("link", id.toString())).build();
    }

}
