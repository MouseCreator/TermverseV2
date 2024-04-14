package mouse.project.lib.web.tool;

public record URLPathNode(String content) implements URLNode {
    @Override
    public String first() {
        return "/";
    }

    @Override
    public String write() {
        return content;
    }

    @Override
    public String next() {
        return "/";
    }
}
