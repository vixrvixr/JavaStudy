package academy.kovalevskyi.javadeepdive.week2.day1;

import academy.kovalevskyi.javadeepdive.week1.day2.AbstractHttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day2.ConcurrentHttpServerWithPath;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class RestServer extends Thread {

    private ConcurrentHttpServerWithPath server;

    private boolean checkMethodAnnotations(Method method) {
        return (method.isAnnotationPresent(Path.class) &&
                (method.isAnnotationPresent(Delete.class)
                || method.isAnnotationPresent(Get.class)
                || method.isAnnotationPresent(Post.class)
                || method.isAnnotationPresent(Put.class)));
    }

    public HttpMethod getHttpMethod(final Method m) {
        if (m.isAnnotationPresent(Delete.class)) {
            return HttpMethod.DELETE;
        }
        if (m.isAnnotationPresent(Get.class)) {
            return HttpMethod.GET;
        }
        if (m.isAnnotationPresent(Post.class)) {
            return HttpMethod.POST;
        }
        return HttpMethod.PUT;
    }

    public RestServer(String packagePath) {
        this.server = new ConcurrentHttpServerWithPath();
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> setControllersClass = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : setControllersClass) {
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                if (checkMethodAnnotations(method)) {
                    HttpMethod httpMethod = getHttpMethod(method);
                    AbstractHttpRequestsHandler controller = new AbstractHttpRequestsHandler(method) {
                        @Override
                        public HttpMethod method() {
                            return httpMethod;
                        }
                    };
                    this.server.addHandler(controller);
                }
            }
        }
    }

    @Override
    public void run() {
        this.server.run();
    }
    public void stopServer() {
        this.server.stopServer();
    }
}
