package mouse.project.lib.web.filter;

public interface FilterChain {
    void invokeNext();
    MFilter getNext();
    boolean hasNext();
}
