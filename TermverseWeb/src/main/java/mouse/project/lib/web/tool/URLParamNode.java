package mouse.project.lib.web.tool;

public record URLParamNode(String name, String value) implements URLNode {

    @Override
    public String first() {
        return "?";
    }

    @Override
    public String write() {
        return name + "=" + value;
    }

    @Override
    public String next() {
        return "&";
    }

}
