package ru.itpark.framework.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Router {
  void route(HttpServletRequest request, HttpServletResponse response);
}
