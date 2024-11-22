package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre");
        return Optional.ofNullable(em.find(Book.class,
                id,
                Map.of("javax.persistence.fetchgraph", entityGraph)));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre");
        return em.createQuery("SELECT b FROM Book b JOIN FETCH b.genres", Book.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public Book save(Book book) {
        if (Objects.isNull(book.getId())) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }
}