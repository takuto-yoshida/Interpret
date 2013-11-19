/**
 * 
 */
package control;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author takuto
 *
 */
public class ObjectDirectAccessor {
	Class<?> c;
	Object instance;

	public Object getHavingObject(){
		return instance;
	}
	
	public Class getHavingClass(){
		return c;
	}
	
	public ObjectDirectAccessor(String className) throws InstantiationException, ClassNotFoundException {
		c = Class.forName(className);
	}

	public Constructor<?>[] getConstracter() {
		return c.getConstructors();
	}

	public Field[] getFieldList() {
		return c.getDeclaredFields();
	}

	public void chengeField(int i, String value) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Field field = null;
		field = c.getDeclaredFields()[i];
		field.setAccessible(true);
		if (field.getType().equals(int.class)) {
			field.set(instance, Integer.valueOf(value));
		} else if (field.getType().equals(short.class)) {
			field.set(instance, Short.valueOf(value));
		} else if (field.getType().equals(byte.class)) {
			field.set(instance, Byte.valueOf(value));
		} else if (field.getType().equals(long.class)) {
			field.set(instance, Long.valueOf(value));
		} else if (field.getType().equals(float.class)) {
			field.set(instance, Float.valueOf(value));
		} else if (field.getType().equals(double.class)) {
			field.set(instance, Double.valueOf(value));
		} else if (field.getType().equals(char.class)) {
			field.set(instance, value.toCharArray()[0]);
		} else if (field.getType().equals(boolean.class)) {
			field.set(instance, Boolean.valueOf(value));
		} else {
			field.set(instance, value);
		}

	}

	public Method[] getMethodList() {
		return c.getMethods();
	}

	public Object callMethod(int i, Object[] objects) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = c.getMethods()[i];
		method.setAccessible(true);

		if (objects == null || objects.length == 0) {
			return method.invoke(instance);
		} else {
			return method.invoke(instance, objects);
		}
	}

	public void createInstance(int index, Object[] objects) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<?> constructor = c.getConstructors()[index];
		instance = constructor.newInstance(objects);
	}

	public Object getFieldValue(int selectedIndex) throws IllegalArgumentException, IllegalAccessException {
		Field field = null;
		field = c.getDeclaredFields()[selectedIndex];
		field.setAccessible(true);
		return field.get(instance);
	}

}
