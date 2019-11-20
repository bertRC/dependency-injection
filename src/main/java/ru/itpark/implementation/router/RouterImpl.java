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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class RouterImpl implements Router {
    private final AutoService autoService;
    private final FileService fileService;

//    private final Pattern detailsPattern = Pattern.compile("/details/(\\d+)$");
//    private final Pattern removePattern = Pattern.compile("/remove/(\\d+)$");
//    private final Pattern imagesPattern = Pattern.compile("/images/(.+)");

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

            // Sample: /details/{id}
            // TODO: обычно парсинг делают через регулярные выражения, но тут простой вариант
            if (url.startsWith("/details/")) {
                if (request.getMethod().equals("GET")) {
                    val id = Integer.parseInt(url.substring("/details/".length()));
                    val item = autoService.getById(id);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher(ResourcesPaths.detailsJsp).forward(request, response);
                    return;
                }

                throw new NotFoundException();
            }

            if (url.startsWith("/remove/")) {
                if (request.getMethod().equals("POST")) {
                    val id = Integer.parseInt(url.substring("/remove/".length()));
                    autoService.removeById(id);
                    response.sendRedirect(rootUrl);
                    return;
                }

                throw new NotFoundException();
            }

            if (url.startsWith("/images/")) {
                if (request.getMethod().equals("GET")) {
                    val id = url.substring("/images/".length());
                    fileService.readFile(id, response.getOutputStream());
                    return;
                }

                throw new NotFoundException();
            }

            if (url.startsWith("/search/")) {
                if (request.getMethod().equals("GET")) {
                    val text = request.getParameter("text");
                    val items = autoService.doSearch(text);
                    request.setAttribute("items", items);
                    request.getRequestDispatcher(ResourcesPaths.searchResultsJsp).forward(request, response);
                }
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

class Main {
    public static void main(String[] args) {
        String text = "/images/123";
        String regex = "/(.+)/(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }
}