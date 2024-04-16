package mouse.project.lib.web.tool;

import java.util.Collection;
import java.util.List;

public interface URLPath {
    List<URLPathNode> getNodes();
    int length();
    void appendAll(Collection<URLPathNode> pathNodes);
    void appendFront(Collection<URLPathNode> nodes);
}
