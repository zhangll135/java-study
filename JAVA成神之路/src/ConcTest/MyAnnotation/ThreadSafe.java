package ConcTest.MyAnnotation;

public @interface ThreadSafe{
    String value() default "Thead Safe";
}
