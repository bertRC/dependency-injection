package ru.itpark.implementation.router;

import lombok.AllArgsConstructor;
import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.controller.AutoController;
import ru.itpark.framework.router.Router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

@Component
@AllArgsConstructor
public class RouterImpl implements Router {
    private final AutoController autoController;

    @Override
    public Object route(HttpServletRequest request, HttpServletResponse response) {
//        switch (request.getRequestURI()) {
//            // mapping -> url -> handler (обработчик)
//            case "/search.do": // search.do?name=...
//                final String name = request.getParameter("name");
//                List<String> result = autoController.doSearch(name);
//                // TODO:
//                // request.setAttribute("result", result);
//                // request.getRequestDispatcher("//").forward(request, response);
//            default:
//                // 404
//                return null;
//        }

        System.out.println("-- Begin --");
        System.out.println("request.getPathInfo()");
        System.out.println(request.getPathInfo());
        System.out.println("request.getContextPath()");
        System.out.println(request.getContextPath());
        Enumeration<String> parameterNames = request.getParameterNames();
        System.out.println("parameterNames");
        System.out.println(parameterNames);
        Enumeration<String> attributeNames = request.getAttributeNames();
        System.out.println("attributeNames");
        System.out.println(attributeNames);
        System.out.println("request.getMethod()");
        System.out.println(request.getMethod());
        System.out.println("request.getQueryString()");
        System.out.println(request.getQueryString());
        System.out.println("-- End --");

        return null;
    }
}
