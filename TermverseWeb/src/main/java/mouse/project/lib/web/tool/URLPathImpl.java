package mouse.project.lib.web.tool;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@EqualsAndHashCode
public class URLPathImpl implements URLPath {

    private final List<URLPathNode> pathNodes;

    public URLPathImpl() {
        pathNodes = new ArrayList<>();
    }

    @Override
    public List<URLPathNode> getNodes() {
        return new ArrayList<>(pathNodes);
    }

    @Override
    public int length() {
        return pathNodes.size();
    }

    @Override
    public void appendAll(Collection<URLPathNode> nodes) {
        pathNodes.addAll(nodes);
    }

    @Override
    public void appendFront(Collection<URLPathNode> nodes) {
        pathNodes.addAll(0, nodes);
    }
}
