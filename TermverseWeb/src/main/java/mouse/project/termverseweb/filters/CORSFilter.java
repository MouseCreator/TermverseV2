package mouse.project.termverseweb.filters;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.filter.MFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class CORSFilter implements MFilter{
    @Override
    public boolean invoke(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return true;
    }


}
