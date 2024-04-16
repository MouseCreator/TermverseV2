package mouse.project.lib.web.scan;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mouse.project.lib.web.invoker.ControllerInvoker;
import mouse.project.lib.web.register.RequestMethod;
import mouse.project.lib.web.tool.FullURL;

@Data
@NoArgsConstructor
public class Registration {
    private FullURL url;
    private RequestMethod requestMethod;
    @EqualsAndHashCode.Exclude
    private ControllerInvoker invoker;

    public Registration(FullURL url, RequestMethod requestMethod, ControllerInvoker invoker) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.invoker = invoker;
    }
}
