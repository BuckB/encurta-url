package br.com.bemobi.encurtaurl.service.impl;

import br.com.bemobi.encurtaurl.domain.Link;
import br.com.bemobi.encurtaurl.repository.LinkRepository;
import br.com.bemobi.encurtaurl.service.LinkService;
import br.com.bemobi.encurtaurl.web.rest.errors.CustomParameterizedException;
import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Service Implementation for managing Link.
 */
@Service
@Transactional
public class LinkServiceImpl implements LinkService{

    private final Logger log = LoggerFactory.getLogger(LinkServiceImpl.class);

    @Inject
    private LinkRepository linkRepository;

    /**
     * Save a link.
     *
     * @param link the entity to save
     * @return the persisted entity
     */
    @Override
    public Link save(Link link) throws Exception {
        log.debug("Request to save Link : {}", link);
        Long tempoInicial = System.nanoTime();

        String urlOriginal = link.getUrl();
        final UrlValidator validador = new UrlValidator(new String[]{"http", "https"});
        if(validador.isValid(urlOriginal)){
            if(link.getAlias() == null) {
                String novoAlias = Hashing.murmur3_32().hashString(urlOriginal, StandardCharsets.UTF_8).toString();
                link.setAlias(novoAlias);
            } else {
                throw new CustomParameterizedException("O alias " + link.getAlias() + " já existe, tente outro.");
            }
            Long tempoFinal = System.nanoTime();
            Long tempoTotal = (tempoFinal - tempoInicial)/1000000;
            link.setTime_taken(tempoTotal);
            Link result = linkRepository.save(link);
            return result;
        } else {
            throw new CustomParameterizedException("Url inválida.");
        }
    }

    /**
     *  Get all the links.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Link> findAll() {
        log.debug("Request to get all Links");
        List<Link> result = linkRepository.findAll();

        return result;
    }

    /**
     *  Get one link by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Link findOne(Long id) {
        log.debug("Request to get Link : {}", id);
        Link link = linkRepository.findOne(id);
        return link;
    }

    @Transactional(readOnly = true)
    public Link findByAlias(String alias){
        log.debug("Request to get Link : {}", alias);
        Link link = linkRepository.findByAlias(alias);
        return link;
    }

    /**
     *  Delete the  link by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Link : {}", id);
        linkRepository.delete(id);
    }
}
