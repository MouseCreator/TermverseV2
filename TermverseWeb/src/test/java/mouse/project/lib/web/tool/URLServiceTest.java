package mouse.project.lib.web.tool;

import mouse.project.lib.web.exception.URLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class URLServiceTest {

    private URLService service;

    @BeforeEach
    void setUp() {
        service = new URLService();
    }

    @Test
    void getPathArgument() {
        String base = "path/[id]/get";
        String input = "path/20/get?i=1";
        FullURL baseURL = service.create(base);
        FullURL inputURL = service.create(input);
        String idValue = service.getPathArgument(baseURL.path(), inputURL.path(), "id");
        assertEquals("20", idValue);
    }

    @Test
    void write() {
        String base = "some/url?a=1&b=2#fragment";
        String expect = "/some/url?a=1&b=2#fragment";
        String[] validUrls = {
                "/" + base,
                base,
                "//" + base,
                "localhost:8080/" + base,
                "https://host.com/" + base};
        List.of(validUrls).forEach(urlStr -> {

            FullURL fullURL = service.create(urlStr);
            String write = service.write(fullURL);
            assertEquals(expect, write);

        });
    }
    private record BaseExpect(String base, String expect) {
    }
    @Test
    void writeShort() {

        BaseExpect[] validUrls = {
            new BaseExpect("host?i=1", "/host?i=1"),
            new BaseExpect("host/e", "/host/e"),
            new BaseExpect("/host#fragment", "/host#fragment")
        };
        List.of(validUrls).forEach(be -> {

            FullURL fullURL = service.create(be.base);
            String write = service.write(fullURL);
            assertEquals(be.expect, write);

        });
    }

    @Test
    void create() {
        String base = "some/url?a=1&b=2#fragment";
        FullURL fullURL = service.create(base);

        URLPath path = fullURL.path();
        assertEquals(2, path.length());

        Object[] pathArray = path.getNodes().stream().map(URLPathNode::content).toArray();
        assertArrayEquals(new String[] {"some","url"}, pathArray);

        Object[] paramNames = fullURL.params().getNodes().stream().map(URLParamNode::name).toArray();
        Object[] paramValues = fullURL.params().getNodes().stream().map(URLParamNode::value).toArray();
        assertArrayEquals(new String[] {"a","b"}, paramNames);
        assertArrayEquals(new String[] {"1","2"}, paramValues);

        URLFragmentNode fragment = fullURL.fragment().getRaw();
        assertEquals("fragment", fragment.write());
    }

    @Test
    void testExtend() {
        String base = "/url/path/";
        String append = "/get/[id]";
        String expected = "/url/path/get/[id]";
        FullURL baseUrl = service.create(base);
        FullURL appended = service.extend(baseUrl, append);
        String fullUrl = service.write(appended);
        assertEquals(expected, fullUrl);
    }

    @Test
    void testInvalid() {
        assertThrows(URLException.class, () -> service.create("host::"));
        assertThrows(URLException.class, () -> service.create("params?i0"));
        assertThrows(URLException.class, () -> service.create("params?i?0"));

    }
}