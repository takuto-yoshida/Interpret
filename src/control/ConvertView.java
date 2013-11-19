package control;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConvertView {

	
	public static String convert(Constructor constructor) {
		StringBuilder sb = new StringBuilder();
		sb.append(constructor.getName());
		sb.append("( ");
		for (Class<?> cls : constructor.getParameterTypes()) {
			sb.append(" ");
			sb.append(cls.getSimpleName());
			sb.append(",");
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append(" )");
		return sb.toString();
	}
	
	public static String convert(Field field) {
		return field.getName() + " - " + field.getType().getName() + ":" + Modifier.toString(field.getModifiers());
	}
	
	public static String convert(Method method) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(method.getName());
		sb.append("( ");
		for (Class<?> cls : method.getParameterTypes()) {
			sb.append(" ");
			sb.append(cls.getSimpleName());
			sb.append(",");
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append(" )");
		return sb.toString();
	}
		
}
