package ConcTest.MyAnnotation;

public @interface GuardeBy {
    String value() default "Not Thread Safe";
}
