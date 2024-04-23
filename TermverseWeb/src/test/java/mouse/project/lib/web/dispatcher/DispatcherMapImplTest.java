package mouse.project.lib.web.dispatcher;

import mouse.project.lib.web.exception.ControllerException;
import mouse.project.lib.web.exception.NotFoundException;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.request.RequestBody;
import mouse.project.lib.web.request.RequestURL;
import mouse.project.lib.web.tool.FullURL;
import mouse.project.lib.web.tool.URLService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DispatcherMapImplTest {
    private DispatcherMap dispatcherMap;

    private final RequestMethod getMethod = RequestMethod.GET;
    @BeforeEach
    void setUp() {
        dispatcherMap = new DispatcherMapImpl(new URLService());
    }

    private final RequestURL emptyRequest = new RequestURL() {
        @Override
        public FullURL getURL() {
            return null;
        }

        @Override
        public RequestMethod method() {return getMethod;}

        @Override
        public RequestBody getBody() {
            return null;
        }

        @Override
        public Map<String, Object> attributes() {
            return null;
        }
    };
    @Test
    void simpleInvoker() {
        ControllerInvoker invoker = r -> "String";
        dispatcherMap.setInvoker("path/to/invoker", getMethod, invoker);
        ControllerInvoker resultInvoker = dispatcherMap.getInvoker("path/to/invoker", getMethod);
        assertEquals("String", resultInvoker.invoke(emptyRequest));
    }

    @Test
    void parametrizedInvoker() {
        ControllerInvoker invoker = r -> "String";
        dispatcherMap.setInvoker("path/to/invoker/[id]/get", getMethod, invoker);
        ControllerInvoker resultInvoker = dispatcherMap.getInvoker("path/to/invoker/1/get", getMethod);
        assertEquals("String", resultInvoker.invoke(emptyRequest));
    }

    @Test
    void testNoDuplicates() {
        ControllerInvoker invoker = r -> "String";
        dispatcherMap.setInvoker("", getMethod, invoker);
        assertThrows(ControllerException.class, () -> dispatcherMap.setInvoker("", getMethod, invoker));
    }

    @Test
    void testNotDefined() {
        assertThrows(NotFoundException.class, () -> dispatcherMap.getInvoker("", getMethod));
        ControllerInvoker invoker = r -> "String";
        dispatcherMap.setInvoker("/path/to", getMethod, invoker);
        assertThrows(NotFoundException.class, () -> dispatcherMap.getInvoker("path/else", getMethod));
    }

    @Test
    void testWrongMethod() {
        ControllerInvoker invoker = r -> "String";
        dispatcherMap.setInvoker("path/to/invoker", getMethod, invoker);
        assertThrows(NotFoundException.class, ()->dispatcherMap.getInvoker("path/to/invoker", RequestMethod.UPDATE));
    }
}