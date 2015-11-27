package pl.edu.agh.io.util;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.reflections.Reflections;

import pl.edu.agh.io.pojo.Trip;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

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

	@SuppressWarnings("unchecked")
	public static List<Trip> getTrips(Date from, Date to){
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Trip.class);

		if (from != null)
			criteria.add(Restrictions.ge("timestamp", from));
		if (to != null)
			criteria.add(Restrictions.le("timestamp", to));
						
		List<Trip> list = criteria
			.add(Restrictions.isNotNull("start"))
			.add(Restrictions.isNotNull("end"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")
//				.add(Projections.property("tripId"), "tripId")
				.add(Projections.property("taxiId"), "taxiId")
				.add(Projections.property("timestamp"), "timestamp")
				.add(Projections.property("start"), "start")
				.add(Projections.property("end"), "end")
				.add(Projections.property("duration"), "duration")
			)
			.addOrder(Order.asc("timestamp"))
			.setResultTransformer(Transformers.aliasToBean(Trip.class))
			.setReadOnly(true)
			.list();
		
		session.close();
		return list;
	}
}
