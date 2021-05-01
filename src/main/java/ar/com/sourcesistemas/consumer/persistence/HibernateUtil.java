package ar.com.sourcesistemas.consumer.persistence;

import ar.com.sourcesistemas.consumer.entities.Data;
import com.fasterxml.classmate.Annotations;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.lang.annotation.Annotation;

public class HibernateUtil
{
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory()
    {
        try
        {
            if (sessionFactory == null)
            {
                Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));
                StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
                configuration.addAnnotatedClass(Data.class);
                serviceRegistryBuilder.applySettings(configuration.getProperties());
                ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }
            return sessionFactory;
        } catch (Throwable ex)
        {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public void shutdown()
    {
        getSessionFactory().close();
    }
}