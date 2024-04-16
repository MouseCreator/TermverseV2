package mouse.project.lib.data.utils;

import mouse.project.lib.ioc.annotation.Service;

import java.util.List;

@Service
public class DaoUtils {
    public String idsList(List<?> ids) {
        StringBuilder builder = new StringBuilder();
        if (ids.isEmpty()) {
            return "()";
        }
        builder.append("(").append(ids.get(0));
        for (int i = 1; i < ids.size(); i++) {
            builder.append(",").append(ids.get(i));
        }
        builder.append(")");
        return builder.toString();
    }

    public String qMarksList(List<?> list) {
        return qMarksList(list.size());
    }

    public String qMarksList(int size) {
        if (size==0) {
            return "()";
        }
        return "(?" +
                ",?".repeat(size - 1) +
                ")";
    }
}
