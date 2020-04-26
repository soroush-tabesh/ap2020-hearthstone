package ir.soroushtabesh.hearthstone.util.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static Session session;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder()
                        .configure()
                        .build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null)
                    StandardServiceRegistryBuilder.destroy(registry);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (session != null)
            session.close();
        if (registry != null)
            StandardServiceRegistryBuilder.destroy(registry);
    }

    public static Session getOpenSession() {
        if (session == null || !session.isOpen())
            session = getSessionFactory().openSession();
        return session;
    }

    private static void rawPush(Object object, Session session) {
        session.saveOrUpdate(object);
    }

    public static void pushSingleObject(Object object, Session session) {
        performTransaction(session, (session1 -> {
            rawPush(object, session1);
            return null;
        }));
    }

    public static void pushSingleObject(Object object) {
        pushSingleObject(object, getOpenSession());
    }

    public static void pushObjects(Object... args) {
        performTransaction((session) -> {
            for (Object arg : args) {
                rawPush(arg, session);
            }
            return null;
        });
    }

    public static void pushObjects(Session session, Object... args) {
        performTransaction(session, (session1) -> {
            for (Object arg : args) {
                rawPush(arg, session1);
            }
            return null;
        });
    }

    private static <T> T rawMerge(T object, Session session) {
        return (T) session.merge(object);
    }

    public static <T> T mergeSingleObject(T object, Session session) {
        return performTransaction(session, session1 -> rawMerge(object, session1));
    }

    public static <T> T mergeSingleObject(T object) {
        return mergeSingleObject(object, getOpenSession());
    }

    public static <T> T performTransaction(Transact<T> transact) {
        return performTransaction(getOpenSession(), transact);
    }

    public static <T> T performTransaction(Session session, Transact<T> transact) {
        session.beginTransaction();
        T res = transact.transact(session);
        session.getTransaction().commit();
        return res;
    }

}
