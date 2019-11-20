package ru.itpark.framework.container;

import ru.itpark.framework.router.Router;
import ru.itpark.implementation.repository.AutoRepository;
import ru.itpark.implementation.router.RouterImpl;
import ru.itpark.implementation.service.AutoService;
import ru.itpark.implementation.service.FileService;
import ru.itpark.implementation.util.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class ContainerStatImpl implements Container {
    @Override
    public Map<Class, Object> init() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        AutoRepository repository = new AutoRepository(jdbcTemplate);
        FileService fileService = new FileService();
        AutoService autoService = new AutoService(repository, fileService);
        Router router = new RouterImpl(autoService, fileService);

        Map<Class, Object> components = new HashMap<>();
        components.put(jdbcTemplate.getClass(), jdbcTemplate);
        components.put(repository.getClass(), repository);
        components.put(fileService.getClass(), fileService);
        components.put(autoService.getClass(), autoService);
        components.put(router.getClass(), router);
        return components;
    }
}
