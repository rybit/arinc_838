package edu.cmu.sv.arinc838.validation.xml.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import edu.cmu.sv.arinc838.builder.Builder;

public class TestUtils {
	public static <T,S extends Builder<?>> void checkPrimitives (T to, Class<S> from, List<Exception> exList) {
		Method[] builderMethods = from.getDeclaredMethods();
		
		for (Method builderM : builderMethods) {
			String builderName = builderM.getName();
			
			if (builderName.startsWith("set")) {
				// this is a setter
				String matchingName = "get".concat (builderName.substring(3));
				try {
					Method matchingMethod = to.getClass ().getMethod(matchingName);
					S builder = from.newInstance();

					Object o = matchingMethod.invoke(to, (Object[])null);
					if (o != null && (o.getClass().isPrimitive() || o instanceof String)) { 
						 builderM.invoke(builder, o);
					}
				} catch (SecurityException e) {
				} catch (NoSuchMethodException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				} catch (InstantiationException e) {
				} catch (Exception e) {
					// only care about these exceptions...hopefully
					exList.add(e);
				}
			}
		}
	}
}
