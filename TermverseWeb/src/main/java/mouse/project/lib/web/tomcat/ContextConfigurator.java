package mouse.project.lib.web.tomcat;

import org.apache.catalina.Context;

public interface ContextConfigurator {
    void config(Context context, Class<?> config);
}
