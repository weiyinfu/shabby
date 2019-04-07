package express4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
void handle(HttpServletRequest req, HttpServletResponse resp);
}
