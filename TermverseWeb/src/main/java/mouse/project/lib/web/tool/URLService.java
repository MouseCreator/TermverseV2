package mouse.project.lib.web.tool;

import mouse.project.lib.ioc.annotation.Service;
import mouse.project.lib.web.exception.URLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class URLService {
    public String getPathArgument(URLPath patternPath, URLPath targetPath, String id) {
        if (patternPath.length() != targetPath.length()) {
            throw new URLException("Pattern and target lengths do not match");
        }
        int len = patternPath.length();
        List<URLPathNode> patternNodes = patternPath.getNodes();
        List<URLPathNode> targetNodes = targetPath.getNodes();
        for (int i = 0; i < len; i++) {
            URLPathNode patternNode = patternNodes.get(i);
            URLPathNode targetNode = targetNodes.get(i);
            Optional<String> argument = getArgument(patternNode);
            if (argument.isEmpty()) {
                continue;
            }
            String s = argument.get();
            if (s.equals(id)) {
                return targetNode.content();
            }
        }
        throw new URLException("Cannot find " + id + " in pattern " + write(patternPath.getNodes()));
    }



    private boolean isArgument(URLPathNode node) {
        String content = node.content();
        return isArgument(content);
    }
    private Optional<String> getArgument(URLPathNode node) {
        if (!isArgument(node)) {
            return Optional.empty();
        }
        String content = node.content();
        int len = content.length();
        return Optional.of(content.substring(1, len-1));
    }

    public String write(FullURL fullURL) {
        URLPath path = fullURL.path();
        String pathString = write(path.getNodes());
        URLParams params = fullURL.params();
        String paramString = write(params.getNodes());
        URLFragment fragment = fullURL.fragment();
        String fragmentString = write(fragment.getAsList());
        return pathString + paramString + fragmentString;
    }

    private String write(List<? extends URLNode> nodes) {
        StringBuilder builder = new StringBuilder();
        writeURLToBuilder(builder, nodes);
        return builder.toString();
    }
    private void writeURLToBuilder(StringBuilder builder, List<? extends URLNode> nodes) {
        int size = nodes.size();
        if (size==0) {
            return;
        }
        int nSize = size - 1;
        builder.append(nodes.getFirst().first() );
        for (int i = 0; i < nSize; i++) {
            builder.append( nodes.get(i).write() );
            builder.append( nodes.get(i).next() );
        }
        builder.append( nodes.get(nSize).write() );
    }
    public FullURL create(String url) {
        url = formatURL(url);
        String[] urlParts = separateURL(url);
        String path = urlParts[0];
        String params = urlParts[1];
        String fragment = urlParts[2];

        URLPath urlPath = new URLPathImpl();
        List<URLPathNode> pathNodes = createPath(path);
        urlPath.appendAll(pathNodes);

        URLParams urlParams = new URLParamsImpl();
        List<URLParamNode> paramNodes = createParams(params);
        urlParams.appendAll(paramNodes);

        URLFragmentNode fragmentNode = new URLFragmentNode(fragment);
        URLFragment urlFragment = new URLFragmentImpl(fragmentNode);

        return new FullURLImpl(urlPath, urlParams, urlFragment);
    }

    private String formatURL(String url) {
        String[] split = url.split(":", 2);
        if (split.length == 2) {
            url = removeHost(split[1]);
        }
        while (url.startsWith("/")) {
            url = url.substring(1);
        }
        return url;
    }

    private String removeHost(String second) {
        String url;
        int doubleSlash = second.indexOf("//");
        if (doubleSlash != -1) {
            second = second.substring(doubleSlash+2);
        }
        int singleSlash = second.indexOf("/");
        if (singleSlash == -1) {
            throw new URLException("Unexpected path: " + second);
        }
        url = second.substring(singleSlash);
        return url;
    }

    private List<URLPathNode> createPath(String path) {
        if (path.isEmpty()) {
            return new ArrayList<>();
        }
        return createFrom(path, "/", URLPathNode::new);
    }

    private List<URLParamNode> createParams(String params) {
        if (params.isEmpty()) {
            return new ArrayList<>();
        }
        return createFrom(params, "&", str ->{
            String[] split = str.split("=");
            if (split.length != 2) {
                if (str.endsWith("=")) {
                    return new URLParamNode(split[0], "");
                }
                throw new URLException("Unexpected parameter structure: " + str);
            }
            return new URLParamNode(split[0], split[1]);
        });
    }

    private <S> List<S> createFrom(String full, String separatedBy, Function<String, S> mapper) {
        String[] split = full.split(separatedBy);
        List<S> result = new ArrayList<>();
        for (String str : split) {
            S s = mapper.apply(str);
            result.add(s);
        }
        return result;
    }

    private String[] separateURL(String url) {
        String[] result = new String[3];

        int fragmentIndex = url.indexOf('#');
        int paramsIndex = url.indexOf('?');

        if (fragmentIndex != -1) {
            result[2] = url.substring(fragmentIndex + 1);
            url = url.substring(0, fragmentIndex);
        } else {
            result[2] = "";
        }

        if (paramsIndex != -1) {
            result[1] = url.substring(paramsIndex + 1);
            result[0] = url.substring(0, paramsIndex);
        } else {
            result[1] = "";
            result[0] = url;
        }

        return result;
    }

    public FullURL extend(FullURL parent, String nextUrl) {
        FullURL fullURL = create(nextUrl);
        fullURL.path().appendFront(parent.path().getNodes());
        return fullURL;
    }

    public boolean isArgument(String str) {
        return (str.startsWith("[") && str.endsWith("]"));
    }
}
