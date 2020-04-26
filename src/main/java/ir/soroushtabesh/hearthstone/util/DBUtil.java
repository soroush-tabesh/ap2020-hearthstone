package ir.soroushtabesh.hearthstone.util;

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

    public static void pushSingleObject(Object object) {
        Session session = getOpenSession();
        pushSingleObject(object, session);
    }

    public static void pushSingleObject(Object object, Session session) {
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();
    }

    public static void mergeSingleObject(Object object, Session session) {
        session.beginTransaction();
        session.merge(object);
        session.getTransaction().commit();
    }


}
