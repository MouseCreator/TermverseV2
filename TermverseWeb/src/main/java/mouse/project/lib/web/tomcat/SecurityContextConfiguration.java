package mouse.project.lib.web.tomcat;

import mouse.project.lib.ioc.annotation.Service;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
@Service
public class SecurityContextConfiguration implements ContextConfigurator {
    @Override
    public void config(Context context, Class<?> config) {
        FilterDef myFilterDef = new FilterDef();
        myFilterDef.setFilterName("FilterDispatcher");
        myFilterDef.setFilterClass("mouse.project.lib.web.filter.FilterDispatcher");
        myFilterDef.addInitParameter("configurationClass", config.getName());
        context.addFilterDef(myFilterDef);

        FilterMap myFilterMap = new FilterMap();
        myFilterMap.setFilterName("FilterDispatcher");
        myFilterMap.addURLPattern("/*");
        context.addFilterMap(myFilterMap);
    }
}
