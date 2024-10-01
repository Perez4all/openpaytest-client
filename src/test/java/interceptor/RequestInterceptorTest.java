package interceptor;

import feign.RequestTemplate;
import feign.Target;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openpay.client.interceptor.RequestInterceptor;
import org.openpay.client.model.MarvelQueriesInfo;
import org.openpay.client.repository.MarvelQueriesInfoRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestInterceptorTest {

    @Mock
    private MarvelQueriesInfoRepository marvelQueriesInfoRepository;

    private RequestInterceptor requestInterceptor;

    @BeforeEach
    public void setup(){
        requestInterceptor = new RequestInterceptor(marvelQueriesInfoRepository);
    }

    @Test
    void testApply(){
        RequestTemplate requestTemplate = Mockito.mock(RequestTemplate.class);
        Map<String, Collection<String>> queries = new HashMap<>();
        queries.put("hash", Collections.singletonList("454352394"));
        doReturn(queries).when(requestTemplate).queries();
        doReturn("GET").when(requestTemplate).method();
        doReturn("url").when(requestTemplate).url();

        Target target = Mockito.mock(Target.class);
        doReturn(target).when(requestTemplate).feignTarget();
        doReturn("characters").when(target).name();


        MarvelQueriesInfo marvelQueriesInfo = new MarvelQueriesInfo();
        marvelQueriesInfo.setStatusCode(200);
        marvelQueriesInfo.setRequestType("GET");

        when(marvelQueriesInfoRepository.save(any(MarvelQueriesInfo.class)))
                .thenReturn(marvelQueriesInfo);

        requestInterceptor.apply(requestTemplate);

        verify(marvelQueriesInfoRepository, times(1)).save(any(MarvelQueriesInfo.class));
    }
}
