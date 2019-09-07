package spymemcached.reactive;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.AbstractListenableFuture;
import net.spy.memcached.internal.BulkFuture;
import net.spy.memcached.internal.BulkGetCompletionListener;
import net.spy.memcached.internal.GenericCompletionListener;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactiveMemcachedTemplateTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MemcachedClient memcachedClient;
    private ReactiveMemcachedTemplate template;

    @BeforeEach
    void setUp() {
        template = new ReactiveMemcachedTemplate(memcachedClient);
    }

    @TestTemplate
    @ExtendWith(TestTemplateInvocationContextProviderImpl.class)
    void lazilyInvokesCorrespondingMethod(Method method) throws Exception {
        Object[] args = stubValuesForMethod(method);

        Mono<?> result = (Mono<?>) method.invoke(template, args);
        verifyZeroInteractions(memcachedClient);

        AtomicReference<Object> reference = new AtomicReference<>();
        result.subscribe(reference::set);
        Future stub = verifyCorrespondingMethodInvoked(method, args);

        GenericCompletionListener listener = verifyListenerAdded(stub);

        String item = UUID.randomUUID().toString();
        when(stub.get()).thenReturn(item);
        when(stub.isDone()).thenReturn(true);

        //noinspection unchecked
        listener.onComplete(null);
        assertThat(reference.get(), equalTo(item));
    }

    private Object[] stubValuesForMethod(Method method) {
        return Arrays.stream(method.getParameters())
                .map(Parameter::getType)
                .map(this::stubValueForType)
                .toArray();
    }

    private Object stubValueForType(Class<?> type) {
        if (type.isArray()) {
            return new String[]{UUID.randomUUID().toString(), UUID.randomUUID().toString()};
        } else if (String.class.equals(type)) {
            return UUID.randomUUID().toString();
        } else if (int.class.equals(type)) {
            return (int) UUID.randomUUID().getLeastSignificantBits();
        } else if (long.class.equals(type)) {
            return UUID.randomUUID().getLeastSignificantBits();
        } else {
            return Mockito.mock(type);
        }
    }

    private Future verifyCorrespondingMethodInvoked(Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        Collection<Invocation> invocations = Mockito.mockingDetails(memcachedClient).getInvocations();
        assertThat(invocations, hasSize(1));
        Invocation invocation = invocations.iterator().next();
        assertThat(invocation.getMethod().getName(), matchesCorrespondingMethodName(method.getName()));
        assertThat(invocation.getRawArguments(), equalTo(args));

        // Invoke the method again to retrieve the backing stub
        return (Future) invocation.getMethod().invoke(memcachedClient, invocation.getRawArguments());
    }

    private Matcher<String> matchesCorrespondingMethodName(String methodName) {
        return anyOf(
                // Either the names match
                equalTo(methodName),
                // Or it is asyncMethod
                equalTo("async" +
                        methodName.substring(0, 1).toUpperCase() +
                        methodName.substring(1)),
                // Or it is asyncMETHOD
                equalTo("async" + methodName.toUpperCase())
        );
    }

    private GenericCompletionListener verifyListenerAdded(Future stub) {
        GenericCompletionListener listener;
        if (stub instanceof BulkFuture) {
            ArgumentCaptor<BulkGetCompletionListener> argumentCaptor = ArgumentCaptor.forClass(BulkGetCompletionListener.class);
            verify((BulkFuture) stub).addListener(argumentCaptor.capture());
            listener = argumentCaptor.getValue();
        } else {
            ArgumentCaptor<GenericCompletionListener> argumentCaptor = ArgumentCaptor.forClass(GenericCompletionListener.class);
            //noinspection unchecked
            verify((AbstractListenableFuture) stub).addListener(argumentCaptor.capture());
            listener = argumentCaptor.getValue();
        }
        return listener;
    }

    static class TestTemplateInvocationContextProviderImpl implements TestTemplateInvocationContextProvider {
        @Override
        public boolean supportsTestTemplate(ExtensionContext extensionContext) {
            return true;
        }

        @Override
        public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
            return Arrays.stream(ReactiveMemcachedOperations.class.getDeclaredMethods())
                    .map(TestTemplateInvocationContextImpl::new);
        }
    }

    static class TestTemplateInvocationContextImpl implements TestTemplateInvocationContext {
        private final Method method;

        TestTemplateInvocationContextImpl(Method method) {
            this.method = method;
        }

        @Override
        public String getDisplayName(int invocationIndex) {
            return method.getName() +
                    "(" +
                    Arrays.stream(method.getParameters())
                            .map(Parameter::getType)
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", ")) +
                    ")";
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return Collections.singletonList(new ParameterResolverImpl(method));
        }
    }

    static class ParameterResolverImpl implements ParameterResolver {
        private final Method method;

        ParameterResolverImpl(Method method) {
            this.method = method;
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext,
                                         ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getIndex() == 0 && parameterContext.getParameter().getType().equals(Method.class);
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext,
                                       ExtensionContext extensionContext) throws ParameterResolutionException {
            return method;
        }
    }

}