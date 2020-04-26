package ir.soroushtabesh.hearthstone.util.db;

import org.hibernate.Session;

public interface Transact<T> {
    T transact(Session session);
}
