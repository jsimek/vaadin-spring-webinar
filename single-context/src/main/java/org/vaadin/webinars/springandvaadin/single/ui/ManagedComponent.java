package org.vaadin.webinars.springandvaadin.single.ui;

/**
 * Alias for {@link org.springframework.stereotype.Component} to prevent conflicts with {@link com.vaadin.ui.Component}.
 *
 * @author petter@vaadin.com
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@org.springframework.stereotype.Component
public @interface ManagedComponent {
    String value() default "";
}