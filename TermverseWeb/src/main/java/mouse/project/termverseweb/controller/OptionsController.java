package mouse.project.termverseweb.controller;

import mouse.project.lib.ioc.annotation.Controller;
import mouse.project.lib.web.annotation.Options;
import mouse.project.lib.web.annotation.RequestPrefix;
import mouse.project.lib.web.annotation.URL;

@Controller
@RequestPrefix("/*")
public class OptionsController {
    @Options
    @URL
    public void options() {
    }
}
