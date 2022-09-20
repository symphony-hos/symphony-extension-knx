package io.symphony.knx.client.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ReflectUtils {

	public static <T> Set<Class<? extends T>> findAllClasses(String packageName, Class<T> type) {
	    Reflections reflections = new Reflections(packageName, Scanners.SubTypes);
	    return reflections.getSubTypesOf(type)
			.stream()
			.collect(Collectors.toSet());
	}
	
	public static Set<Field> getStaticFields(Class<?> clazz) {
		Field[] declaredFields = clazz.getDeclaredFields();
		Set<Field> staticFields = new HashSet<Field>();
		for (Field field : declaredFields) {
		    if (Modifier.isStatic(field.getModifiers())) {
		        staticFields.add(field);
		    }
		}
		return staticFields;
	}
	
}
