package org.monkey.common.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DirectoryScanningSpec {

    public abstract String testSourceRoot();

    public abstract String[] filesToInclude();

    public abstract String[] filesToExclude() default {};
}
