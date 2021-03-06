package ningbo.media.core.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import ningbo.media.core.dao.BaseDao;
import ningbo.media.core.page.Finder;
import ningbo.media.core.page.Pagination;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;

public class BaseDaoImpl<E, PK extends Serializable> implements BaseDao<E, PK> {

	@Resource
	private HibernateTemplate hibernateTemplate;

	private Class<E> entityClass;

	public BaseDaoImpl() {

	}

	public BaseDaoImpl(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public E get(PK id) {
		Assert.notNull(id, "id is required.");
		return (E) getHibernateTemplate().get(this.entityClass, id);
	}

	public E load(PK id) {
		Assert.notNull(id, "id is required.");
		return (E) getHibernateTemplate().load(this.entityClass, id);
	}
	
	public void persist(E entity){
		Assert.notNull(entity, "entity is required");
		getHibernateTemplate().persist(entity) ;
	}

	public List<E> get(final PK[] ids) {
		Assert.notEmpty(ids, "ids must not be empty!");
		final String hql = "from " + entityClass.getName()
				+ " as b where b.id in(:ids) ";
		return getHibernateTemplate().execute(new HibernateCallback<List<E>>() {

			@SuppressWarnings("unchecked")
			public List<E> doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createQuery(hql).setParameterList("ids", ids)
						.list();

			}

		});
	}

	@SuppressWarnings("unchecked")
	public E get(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model."
				+ propertyName + " = ?";
		return (E) findUnique(hql, value);
	}

	public List<E> getList(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model."
				+ propertyName + " = ?";
		return (List<E>) findByHql(hql, value);
	}

	public List<E> getAll() {
		String hql = "from " + entityClass.getName();
		return (List<E>) findByHql(hql);
	}

	public Long getTotalCount() {
		String hql = "select count(*) from " + entityClass.getName();
		return (Long) findUnique(hql);
	}

	public boolean isExist(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		E entity = get(propertyName, value);
		return (entity != null);
	}

	@SuppressWarnings("unchecked")
	public PK save(E entity) {
		Assert.notNull(entity, "entity is required");
		return (PK) getHibernateTemplate().save(entity);
	}

	public void update(E entity) {
		Assert.notNull(entity, "entity is required");
		try{
			clear();
			//getHibernateTemplate().merge(entity);
			getHibernateTemplate().update(entity);
			//getHibernateTemplate().saveOrUpdate(entity);
			flush();
		}catch(Exception ex){
			ex.printStackTrace() ;
		}
	}

	public void delete(E entity) {
		Assert.notNull(entity, "entity is required");
		getHibernateTemplate().delete(entity);

	}

	public void delete(PK id) {
		Assert.notNull(id, "id is required");
		getHibernateTemplate().delete(load(id));
	}

	public void delete(PK[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		for (PK id : ids) {
			E entity = load(id);
			getHibernateTemplate().delete(entity);
		}
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

	public void clear() {
		getHibernateTemplate().clear();
	}

	public void evict(Object object) {
		getHibernateTemplate().evict(object);
	}

	public Object findUnique(final String hql, final Object... values) {
		return getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0, j = values.length; i < j; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.uniqueResult();
			}

		});
	}

	public List<E> findByHql(final String hql, final Object... values) {
		return findByHql(hql, false, null, values);
	}

	public List<E> findByHql(final String hql, final boolean isLike,
			final Integer limit, final Object... values) {

		return getHibernateTemplate().execute(new HibernateCallback<List<E>>() {

			@SuppressWarnings("unchecked")
			public List<E> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);

				if (limit != null) {
					query.setMaxResults(limit);
				}
				if (values != null) {
					if (isLike) {
						for (int i = 0, j = values.length; i < j; i++) {
							query.setParameter(i, "%"
									+ String.valueOf(values[i]) + "%");
						}
					} else {
						for (int i = 0, j = values.length; i < j; i++) {
							query.setParameter(i, values[i]);
						}
					}
				}
				return query.list();
			}

		});
	}

	public List<String> findAllObject(final String hql, final Object... values) {
		return getHibernateTemplate().execute(
				new HibernateCallback<List<String>>() {

					@SuppressWarnings("unchecked")
					public List<String> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						if (values != null) {
							for (int i = 0, j = values.length; i < j; i++) {
								query.setParameter(i, values[i]);
							}
						}
						return query.list();
					}

				});
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public Pagination<E> findByPage(final Finder finder, int pageNo,
			int pageSize) {
		int totalCount = getTotalCount().intValue();
		final Pagination<E> p = new Pagination<E>(pageNo, pageSize, totalCount);
		if (totalCount < 1) {
			p.setList(new ArrayList<E>());
			return p;
		}
		return getHibernateTemplate().execute(
				new HibernateCallback<Pagination<E>>() {
					@SuppressWarnings("unchecked")
					public Pagination<E> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(finder.getOrigHql());
						finder.setParamsToQuery(query);
						query.setFirstResult(p.getFirstResult());
						query.setMaxResults(p.getPageSize());
						if (finder.isCacheable()) {
							query.setCacheable(true);
						}
						List<E> list = query.list();
						p.setList(list);
						return p;
					}
				});
	}

	
	public List<E> findByPage(final Finder finder) {
		return getHibernateTemplate().execute(new HibernateCallback<List<E>>() {
			@SuppressWarnings("unchecked")
			public List<E> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = finder.createQuery(session) ;
				List<E> list = query.list() ;
				return list;
			}

		});

	}

}
