package homework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class StatRun {
    public int success = 0;
    public int error = 0;
}

class Testing {

    private String className;
    StatRun statRunBefore = new StatRun();
    StatRun statRunTest = new StatRun();
    StatRun statRunAfter = new StatRun();

    void setClassName(String className) {
        this.className = className;
    }

    static void invokeMethod(Object object, Method method, StatRun statRun) {
        try {
            System.out.println("exec " + method.getName());
            var result = method.invoke(object);
            System.out.println("result:" + result);
            statRun.success++;
        } catch (Exception e) {
            System.err.println("error:" + e.getMessage());
            statRun.error++;
        }
    }

    static void invokeMethodList(Object object, ArrayList<Method> methodList, String name, StatRun statRun) {
        if (methodList.size() > 0) {
            System.out.println("===========================");
            System.out.println("Start " + name);

            for (Method method : methodList) {
                invokeMethod(object, method, statRun);
            }
            System.out.println("End " + name);
            System.out.println("===========================");
        }
    }

    public void run() throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> clazz = Class.forName(this.className);

        System.out.println("Run test Class Name:" + clazz.getSimpleName() +
                           " canonicalName:" + clazz.getCanonicalName());

        Method[] methodsAll = clazz.getDeclaredMethods();
        ArrayList<Method> methodsBefore = new ArrayList<>();
        ArrayList<Method> methodsTest = new ArrayList<>();
        ArrayList<Method> methodsAfter = new ArrayList<>();
        for (Method method : methodsAll) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getName().equals("ru.otus.annotations.Before")) {
                    methodsBefore.add(method);
                } else if (annotation.annotationType().getName().equals("ru.otus.annotations.Test")) {
                    methodsTest.add(method);
                } else if (annotation.annotationType().getName().equals("ru.otus.annotations.After")) {
                    methodsAfter.add(method);
                }
            }
        }

        for (Method method : methodsTest) {
            Constructor<?>[] constructors = clazz.getConstructors();
            Object object = constructors[0].newInstance();

            invokeMethodList(object, methodsBefore, "Before", statRunBefore);

            System.out.println("Start Test");
            invokeMethod(object, method, statRunTest);
            System.out.println("End Test");

            invokeMethodList(object, methodsAfter, "After", statRunAfter);
        }
    }
}

public class RunTest {

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            Testing testing = new Testing();
            testing.setClassName(arg);
            testing.run();
            System.out.println("Total testing completed: success " + (testing.statRunBefore.success +
                                                                      testing.statRunTest.success +
                                                                      testing.statRunAfter.success) +
                                                         " error " + (testing.statRunBefore.error +
                                                                      testing.statRunTest.error +
                                                                      testing.statRunAfter.error));

            System.out.println("Of them Before: success " + testing.statRunBefore.success + " error " + testing.statRunBefore.error);
            System.out.println("Of them Test: success " + testing.statRunTest.success + " error " + testing.statRunTest.error);
            System.out.println("Of them After: success " + testing.statRunAfter.success + " error " + testing.statRunAfter.error);
        }
    }
}
