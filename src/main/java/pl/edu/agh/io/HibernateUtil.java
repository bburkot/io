package pl.edu.agh.io;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.proxy.HibernateProxy;
import org.reflections.Reflections;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	static class PrintStatistics extends Thread {
		private boolean val;

		public PrintStatistics(boolean val) {
			this.val = val;
		}

		public void run() {
			while (val) {
				try {
					sleep(10 * 1000);
					System.out.println(sessionFactory.getStatistics());
				} catch (Exception e) {
					return;
				}
			}
		}
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration conf = new Configuration()
						.configure("hibernate.cfg.xml");

				Reflections reflections = new Reflections("pl.edu.agh.io.pojo");
				Set<Class<?>> classes = reflections
						.getTypesAnnotatedWith(Entity.class);
				for (final Class<?> clazz : classes)
					conf.addAnnotatedClass(clazz);

				StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
						.applySettings(conf.getProperties()).build();
				sessionFactory = conf.buildSessionFactory(ssr);

				new PrintStatistics(false).start();

			} catch (Throwable e) {
				System.err.println("Error in creating SessionFactory object: "
						+ e.getMessage());
				throw new ExceptionInInitializerError(e);
			}
		}
		return sessionFactory;
	}

	public static String saveOrUpdate(Object obj) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
			session.getTransaction().commit();
			return null;
		}
		catch (Throwable ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
			return ex.getMessage();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object getById(Class clazz, Serializable id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Object obj = session.get(clazz, id);
		session.close();
		return obj;
	}

	public static String delete(Object obj) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();
		try {
			session.delete(obj);
			session.getTransaction().commit();
			return null;
		} catch (Throwable ex) {
			session.getTransaction().rollback();
			ex.printStackTrace();
			return ex.getMessage();
		} finally {
			session.close();
		}
	}


	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
	    if (entity == null) 
	        throw new NullPointerException();

	    Hibernate.initialize(entity);
	    if (entity instanceof HibernateProxy) {
	        entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
	                .getImplementation();
	    }
	    return entity;
	}
	
}
