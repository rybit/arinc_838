package edu.cmu.sv.arinc838.validation.xml.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import edu.cmu.sv.arinc838.builder.Builder;

public class TestUtils {
	public static <T, S extends Builder<?>> void checkPrimitives(T from,
			Class<S> to, List<Exception> exList) {
		Method[] builderMethods = to.getDeclaredMethods();

		for (Method builderM : builderMethods) {
			String builderName = builderM.getName();

			if (builderName.startsWith("set")) {
				// this is a setter
				try {
					Method matchingMethod = getAccessor(from.getClass(), "get",
							builderName.substring(3));

					// TODO needs a test
					if (matchingMethod == null) {
						matchingMethod = getAccessor(from.getClass(), "is",
								builderName.substring(3));
					}

					if (matchingMethod != null) {
						S builder = to.newInstance();

						Object o = matchingMethod.invoke(from, (Object[]) null);
						if (isPrimitiveType(o)) {
							builderM.invoke(builder, o);
						}
					}
				} catch (IllegalAccessException e) {
				} catch (InstantiationException e) {
				} catch (InvocationTargetException e) {
					// only care about these exceptions...hopefully
					exList.add(new Exception(e.getTargetException()));
				}
			}
		}
	}

	private static Method getAccessor(Class<?> clazz, String prefix, String base) {
		String matchingName = prefix.concat(base);
		Method accessor = null;
		try {
			accessor = clazz.getMethod(matchingName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		return accessor;
	}

	/**
	 * This method checks if the object is a primitive type. Because of Java's
	 * auto-boxing of true primitive types to their respective Object
	 * representations, it is not sufficient to call
	 * "o.getClass().isPrimitive()" alone.
	 * 
	 * @param o
	 *            The object to check
	 * @return True if the type is a true primitive (boolean, byte, char, short,
	 *         int, long, float, or double), an auto-boxed object (Boolean,
	 *         Byte, Character, Short, Integer, Long, Float, Double), or a
	 *         String
	 */
	private static boolean isPrimitiveType(Object o) {
		return o != null
				&& (o.getClass().isPrimitive() || o instanceof String
						|| o instanceof Long || o instanceof Integer
						|| o instanceof Short || o instanceof Double
						|| o instanceof Float || o instanceof Character
						|| o instanceof Byte || o instanceof Boolean);
	}
}
