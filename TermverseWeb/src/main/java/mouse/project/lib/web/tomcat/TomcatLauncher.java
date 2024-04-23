package mouse.project.lib.web.tomcat;

import mouse.project.lib.ioc.Ioc;
import mouse.project.lib.web.mapper.WebMapper;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collection;

public class TomcatLauncher {
    private Tomcat tomcat;
    private boolean running = false;
    private static final Logger logger = LogManager.getLogger(TomcatLauncher.class);
    public Connector launch(Class<?> configClass) throws Exception {
        if (running) {
            return null;
        }
        String webappDirLocation = "src/main/webapp";
        Tomcat tomcat = new Tomcat();

        int webPort = 8080;

        tomcat.setPort(webPort);

        Context context = tomcat.addContext("", null);
        Tomcat.addServlet(context, "WebMapper", new WebMapper(configClass));

        Collection<ContextConfigurator> configs = Ioc.getConfiguredInjector(configClass).getAll(ContextConfigurator.class);
        configs.forEach(c -> c.config(context, configClass));

        context.addServletMappingDecoded("/*", "WebMapper");
        logger.debug("Configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        tomcat.start();
        running = true;

        logger.debug("Web app started. Listening to post: " + webPort);
        Connector connector = tomcat.getConnector();
        tomcat.getServer().await();
        return connector;
    }
    public void stop() throws Exception {
        if (!running) { return; }
        tomcat.getServer().stop();
        running = false;
        tomcat = null;
    }
}
