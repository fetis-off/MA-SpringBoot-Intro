package mate.academy.springbootintro.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.exception.DataProcessingException;
import mate.academy.springbootintro.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Cant insert book into DB: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        List<Book> books;
        try (Session session = sessionFactory.openSession()) {
            books = session.createQuery("from Book ").list();
            return books;
        } catch (Exception e) {
            throw new DataProcessingException("Cant find all books", e);
        }
    }
}
