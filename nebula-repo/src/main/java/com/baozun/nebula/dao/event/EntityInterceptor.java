package com.baozun.nebula.dao.event;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.security.crypto.PIIEncryptionModule;
import com.baozun.nebula.security.crypto.PIIField;

public class EntityInterceptor extends EmptyInterceptor {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private PIIEncryptionModule piiEncryptionModule;

	private int getFieldIndex(String fieldName, String[] propertyNames) {
		for (int i = 0; i < propertyNames.length; i++) {
			if (fieldName.equals(propertyNames[i])) {
				return i;
			}
		}
		throw new RuntimeException("field not found for encryption.");
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		if (null == entity) {
			return true;
		}

		// 如果有标注字段，对其进行加密
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(PIIField.class) != null
					&& f.getType().toString().endsWith("String")) {
				int i = getFieldIndex(f.getName(), propertyNames);
				if (state[i] != null) {
					state[i] = piiEncryptionModule.encrypt((String) state[i]);
				}
			}
		}
		return true;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		// TODO Auto-generated method stub
		return super.onFlushDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {

		if (null == entity) {
			return true;
		}

		// 如果有标注字段，对其进行解密
		Field[] fields = entity.getClass().getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(PIIField.class) != null
					&& f.getType().toString().endsWith("String")) {
				int i = getFieldIndex(f.getName(), propertyNames);
				if (state[i] != null) {
					state[i] = piiEncryptionModule.decrypt((String) state[i]);
				}
			}
		}
		return true;
	}

	@Override
	public void postFlush(Iterator entities) {
		// TODO Auto-generated method stub
		super.postFlush(entities);
	}

	@Override
	public void preFlush(Iterator entities) {
		// TODO Auto-generated method stub
		super.preFlush(entities);
	}

	@Override
	public Boolean isTransient(Object entity) {
		// TODO Auto-generated method stub
		return super.isTransient(entity);
	}

	@Override
	public Object instantiate(String entityName, EntityMode entityMode,
			Serializable id) {
		// TODO Auto-generated method stub
		return super.instantiate(entityName, entityMode, id);
	}

	@Override
	public int[] findDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		// TODO Auto-generated method stub
		return super.findDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}

	@Override
	public String getEntityName(Object object) {
		// TODO Auto-generated method stub
		return super.getEntityName(object);
	}

	@Override
	public Object getEntity(String entityName, Serializable id) {
		// TODO Auto-generated method stub
		return super.getEntity(entityName, id);
	}

	@Override
	public void afterTransactionBegin(Transaction tx) {
		// TODO Auto-generated method stub
		super.afterTransactionBegin(tx);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		// TODO Auto-generated method stub
		super.afterTransactionCompletion(tx);
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		// TODO Auto-generated method stub
		super.beforeTransactionCompletion(tx);
	}

	@Override
	public String onPrepareStatement(String sql) {
		// TODO Auto-generated method stub
		return super.onPrepareStatement(sql);
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key)
			throws CallbackException {
		// TODO Auto-generated method stub
		super.onCollectionRemove(collection, key);
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key)
			throws CallbackException {
		// TODO Auto-generated method stub
		super.onCollectionRecreate(collection, key);
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key)
			throws CallbackException {
		// TODO Auto-generated method stub
		super.onCollectionUpdate(collection, key);
	}


}
