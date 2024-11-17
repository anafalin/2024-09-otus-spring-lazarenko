package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class,
                id,
                Map.of("javax.persistence.fetchgraph", getGraph("authors-entity-book-graph"))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class)
                .setHint("javax.persistence.fetchgraph", getGraph("authors-entity-book-graph"))
                .getResultList();
    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (Objects.isNull(book.getId())) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }

    private Object getGraph(String name) {
        return em.getEntityGraph(name);
    }
}