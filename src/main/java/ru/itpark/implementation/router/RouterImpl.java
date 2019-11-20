package ru.itpark.implementation.router;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.itpark.implementation.model.Auto;
import ru.itpark.exceptions.NotFoundException;
import ru.itpark.framework.annotation.Component;
import ru.itpark.framework.router.Router;
import ru.itpark.implementation.service.AutoService;
import ru.itpark.implementation.service.FileService;
import ru.itpark.implementation.util.ResourcesPaths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class RouterImpl implements Router {
    private final AutoService autoService;
    private final FileService fileService;

    public static final Pattern urlPattern = Pattern.compile("/(.+)/(.*)$");

    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) {
        try {
            val rootUrl = request.getContextPath().isEmpty() ? "/" : request.getContextPath();
            val url = request.getRequestURI().substring(request.getContextPath().length());

            if (url.equals("/")) {
                if (request.getMethod().equals("GET")) {
                    val items = autoService.getAll();
                    request.setAttribute("items", items);
                    request.getRequestDispatcher(ResourcesPaths.frontpageJsp).forward(request, response);
                    return;
                }

                if (request.getMethod().equals("POST")) {
                    val name = request.getParameter("name");
                    val description = request.getParameter("description");
                    val part = request.getPart("image");
                    autoService.create(new Auto(0, name, description, null), part);
                    response.sendRedirect(rootUrl);
                    return;
                }

                throw new NotFoundException();
            }

            val matcher = urlPattern.matcher(url);
            String queryName;
            String attribute;
            // url template: "/queryName/attribute"
            if (matcher.find()) {
                queryName = matcher.group(1);
                attribute = matcher.group(2);
            } else return;

            if (queryName.equals("details")) {
                if (request.getMethod().equals("GET")) {
                    val id = Long.parseLong(attribute);
                    val item = autoService.getById(id);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher(ResourcesPaths.detailsJsp).forward(request, response);
                    return;
                }

                throw new NotFoundException();
            }

            if (queryName.equals("remove")) {
                if (request.getMethod().equals("POST")) {
                    val id = Long.parseLong(attribute);
                    autoService.removeById(id);
                    response.sendRedirect(rootUrl);
                    return;
                }

                throw new NotFoundException();
            }

            if (queryName.equals("images")) {
                if (request.getMethod().equals("GET")) {
                    val id = attribute;
                    fileService.readFile(id, response.getOutputStream());
                    return;
                }

                throw new NotFoundException();
            }

            if (queryName.equals("search")) {
                if (request.getMethod().equals("GET")) {
                    val text = request.getParameter("text");
                    val items = autoService.doSearch(text);
                    request.setAttribute("items", items);
                    request.getRequestDispatcher(ResourcesPaths.searchResultsJsp).forward(request, response);
                }

                throw new NotFoundException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.getRequestDispatcher(ResourcesPaths.errorJsp).forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
