package mouse.project.lib.web.invoker;

import mouse.project.lib.web.invoker.desc.ArgumentDesc;
import mouse.project.lib.web.request.RequestURL;

import java.util.List;

public class MethodArgumentsImpl implements MethodArguments {
    private final List<ArgumentDesc> argumentDescList;
    public MethodArgumentsImpl(List<ArgumentDesc> argumentDescList) {
        this.argumentDescList = argumentDescList;
    }

    @Override
    public Object[] getArguments(RequestURL requestURL) {
        int size = argumentDescList.size();
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            ArgumentDesc argumentProcessor = argumentDescList.get(i);
            Object obj = argumentProcessor.apply(requestURL);
            result[i] = obj;
        }
        return result;
    }
}
