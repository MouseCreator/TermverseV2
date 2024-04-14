package mouse.project.lib.web.tool;

import java.util.Collection;
import java.util.List;

public interface URLParams {
    List<URLParamNode> getNodes();
    int length();
    void appendAll(Collection<URLParamNode> nodes);
}
