package org.acme.example.jfr;

import jdk.jfr.*;

@Name(RestEndpointInvocationEvent.NAME)
@Label("RestController methods Invocation")
@Category("RestController")
@Description("Invocation of a RestController method annotated with @RequestMapping")
@StackTrace(false)
@Threshold("1 s")
public class RestEndpointInvocationEvent extends Event {

    static final String NAME = "org.acme.example.RestController";

    @Label("Resource Method")
    public String method;

    @Label("Media Type")
    public String mediaType;

    @Label("Path")
    public String path;

    @Label("Query Parameters")
    public String queryParameters;

    @Label("Headers")
    public String headers;

    @Label("Length")
    @DataAmount
    public int length;

    @Label("Response Headers")
    public String responseHeaders;

    @Label("Response Length")
    public int responseLength;

    @Label("Response Status")
    public int status;

    @Label("Path Processor")
    @SettingDefinition
    protected boolean processPath(PathProcessorControl process) {
        return process.matches(path);
    }

}
