package net.kencochrane.raven.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import net.kencochrane.raven.event.interfaces.ImmutableThrowable;
import net.kencochrane.raven.event.interfaces.StackTraceInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestStackTraceInterfaceBinding extends AbstractTestInterfaceBinding {
    private StackTraceInterfaceBinding interfaceBinding;
    @Mock
    private StackTraceInterface mockStackTraceInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        interfaceBinding = new StackTraceInterfaceBinding();
    }

    @Test
    public void testSingleStackFrame() throws Exception {
        String methodName = UUID.randomUUID().toString();
        String className = UUID.randomUUID().toString();
        int lineNumber = 1;
        StackTraceElement stackTraceElement = new StackTraceElement(className, methodName, null, lineNumber);
        when(mockStackTraceInterface.getStackTrace()).thenReturn(new StackTraceElement[]{stackTraceElement});

        JsonGenerator jSonGenerator = getJsonGenerator();
        interfaceBinding.writeInterface(jSonGenerator, mockStackTraceInterface);
        jSonGenerator.close();

        JsonNode frames = getMapper().readValue(getJsonParser(), JsonNode.class).get("frames");
        assertThat(frames.size(), is(1));
        assertThat(frames.get(0).get("module").asText(), is(className));
        assertThat(frames.get(0).get("function").asText(), is(methodName));
        assertThat(frames.get(0).get("lineno").asInt(), is(lineNumber));
    }
}
