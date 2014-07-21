package org.rs.jug.bdd;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Steps {

	
	Class<?>[] value();
}
