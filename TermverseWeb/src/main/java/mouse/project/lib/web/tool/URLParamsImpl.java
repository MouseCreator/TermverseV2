package mouse.project.lib.web.tool;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@EqualsAndHashCode
public class URLParamsImpl implements URLParams {

    private final List<URLParamNode> paramNodes;
    public URLParamsImpl() {
        this.paramNodes = new ArrayList<>();
    }

    @Override
    public List<URLParamNode> getNodes() {
        return new ArrayList<>(paramNodes);
    }

    @Override
    public int length() {
        return paramNodes.size();
    }

    @Override
    public void appendAll(Collection<URLParamNode> nodes) {
        paramNodes.addAll(nodes);
    }


}
