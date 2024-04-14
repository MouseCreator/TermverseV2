package mouse.project.lib.web.tool;

import lombok.EqualsAndHashCode;

import java.util.List;
@EqualsAndHashCode
public class URLFragmentImpl implements URLFragment {
    private final URLFragmentNode node;
    public URLFragmentImpl(URLFragmentNode fragmentNode) {
        node = fragmentNode;
    }

    @Override
    public List<URLFragmentNode> getAsList() {
        if (node.fragment().isEmpty()) {
            return List.of();
        }
        return List.of(node);
    }

    @Override
    public URLFragmentNode getRaw() {
        return node;
    }
}
