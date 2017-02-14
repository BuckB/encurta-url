//package br.com.bemobi.encurtaurl.repository;
//
//import br.com.bemobi.encurtaurl.domain.Link;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//
//public class LinkRepositoryImpl implements LinkRepository {
//
//    public LinkRepositoryImpl() {
//    }
//
//    @Override
//    public Link findByAlias(String alias) {
//        Query query = EntityManager.createNamedQuery("findByAlias", Link.class);
//        query.setParameter(alias, alias);
//        Link result = (Link) query.getSingleResult();
//        return result;
//    }
//}
