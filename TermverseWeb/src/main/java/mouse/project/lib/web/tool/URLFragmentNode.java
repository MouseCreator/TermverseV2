package mouse.project.lib.web.tool;

public record URLFragmentNode(String fragment) implements URLNode {
    @Override
    public String first() {
        return "#";
    }

    @Override
    public String write() {
        return fragment;
    }
    @Override
    public String next() {
        throw new UnsupportedOperationException("URL Anchor has no next element");
    }
}
