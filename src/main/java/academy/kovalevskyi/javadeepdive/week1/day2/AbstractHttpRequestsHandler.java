package academy.kovalevskyi.javadeepdive.week1.day2;

import academy.kovalevskyi.javadeepdive.week2.day0.JsonHelper;
import academy.kovalevskyi.javadeepdive.week2.day1.Path;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public abstract class AbstractHttpRequestsHandler implements HttpRequestsHandler{
    protected Method methodReflect;

    protected AbstractHttpRequestsHandler(Method method) {
        this.methodReflect = method;
    }

    @Override
    public String path() {
        return methodReflect.getAnnotation(Path.class).value();
    }

//    public HttpResponse.Builder status(HttpResponse.ResponseStatus status)
//    public HttpResponse.Builder contentType(ContentType contentType)
//    public HttpResponse.Builder body(String body)
//    public HttpResponse.Builder httpVersion(HttpVersion version)

    @Override
    public HttpResponse process(HttpRequest request) {
        try {
            Object obj = this.methodReflect.getDeclaringClass().getDeclaredConstructor().newInstance();
            if (this.methodReflect.getParameterCount() == 0) {
                String s = JsonHelper.toJsonString(this.methodReflect.invoke(obj));
                return new HttpResponse.Builder()
                        .httpVersion(request.httpVersion())
                        .status(HttpResponse.ResponseStatus.OK)
                        .contentType(ContentType.APPLICATION_JSON)
                        .body(s == null ? "" : s)
                        .build();
            }
            // Parameters > 0
            Parameter[] parameters = this.methodReflect.getParameters();
            Object[] args = new Object[this.methodReflect.getParameterCount()];
            int i = 0;
            for (Parameter parameter : parameters) {
                if (!request.body().isEmpty()) {
                    // request.body() - don't work with many params, need to parse body
                    args[i] = JsonHelper.fromJsonString(request.body().orElse(""), parameter.getType());
                }
                i++;
            }
            this.methodReflect.invoke(obj, args);
            return HttpResponse.OK_200;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResponse.ERROR_500;
    }
}