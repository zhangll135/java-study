package ConcTest.MyAnnotation;

public @interface NotThreadSafe{
    String value() default "Not Thread Safe";
}
