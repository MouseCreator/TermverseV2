package mouse.project.lib.web.filter;

public interface FilterChain {
    void invokeNext();
    Filter getNext();
    boolean hasNext();
}
