package ru.itpark.implementation.router;

import lombok.AllArgsConstructor;
import ru.itpark.domain.Auto;
import ru.itpark.framework.annotation.Component;
import ru.itpark.implementation.controller.AutoController;
import ru.itpark.framework.router.Router;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
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

        System.out.println("request.getMethod()");
        System.out.println(request.getMethod());
        System.out.println("request.getPathInfo()");
        System.out.println(request.getPathInfo());
        System.out.println("request.getRequestURI()");
        System.out.println(request.getRequestURI());

        String method = request.getMethod();
        if ((method.equals("GET")) && (request.getPathInfo() != null) && (request.getRequestURI().startsWith("/images"))) {
            String[] parts = request.getPathInfo().split("/");
            if (parts.length != 2) {
                throw new RuntimeException("Not found");
            }
            try {
                autoController.readFile(parts[1], response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (method.equals("GET")) {
            try {
                request.setAttribute("items", autoController.getAll());
                request.getRequestDispatcher(ResourcesPaths.catalogJspPath).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }

        } else if (method.equals("POST")) {
            try {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                Part part = request.getPart("file");

                String image = autoController.writeFile(part);

//            autoService.create(name, description, image);
                autoController.create(new Auto(0, name, description, image));
                System.out.println("request.getContextPath()");
                System.out.println(request.getContextPath());
                System.out.println("request.getServletPath()");
                System.out.println(request.getServletPath());
                response.sendRedirect(String.join("/", request.getContextPath(), request.getServletPath()));
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
