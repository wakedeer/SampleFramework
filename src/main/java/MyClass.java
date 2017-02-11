import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.*;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@interface CheckNameField{
    String value() default "c";
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
}

public class MyClass {

    int a = 1;
    @MyAnnotation
    int b = 2;
    @CheckNameField
    int c = 3;

    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        Class<?> clazz = myClass.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try(PrintWriter out = new PrintWriter(new File("test.xml"))){

            out.println("<xml>");
            for (Field f : fields) {
                if(f.isAnnotationPresent(CheckNameField.class)&& f.getName()!="c")
                    out.println("error");
                if(!f.isAnnotationPresent(MyAnnotation.class)) {
                    String nameField = f.getName();
                    try {
                        int value = (Integer)f.get(myClass);
                        out.println("<"+nameField+">"+value+"</"+nameField+">");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            out.println("</xml>");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
