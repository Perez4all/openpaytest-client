package interceptor;

import feign.InvocationContext;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openpay.client.interceptor.ResponseInterceptor;
import org.openpay.client.model.MarvelQueriesInfo;
import org.openpay.client.repository.MarvelQueriesInfoRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResponseInterceptorTest {

    private ResponseInterceptor responseInterceptor;

    @Mock
    private MarvelQueriesInfoRepository marvelQueriesInfoRepository;

    @BeforeEach
    public void setup(){
        this.responseInterceptor = new ResponseInterceptor(marvelQueriesInfoRepository);
    }

    @Test
    void testIntercept() throws Exception {
        InvocationContext invocationContext = Mockito.mock(InvocationContext.class);
        feign.ResponseInterceptor.Chain chain = mock(feign.ResponseInterceptor.Chain.class);
        Response response = Mockito.mock(Response.class);
        Request request = mock(Request.class);
        RequestTemplate requestTemplate = mock(RequestTemplate.class);
        doReturn(response).when(invocationContext).response();
        doReturn(request).when(response).request();
        doReturn(200).when(response).status();
        doReturn("any").when(response).reason();
        doReturn(requestTemplate).when(request).requestTemplate();
        doReturn(new Object()).when(invocationContext).proceed();

        Map<String, Collection<String>> queries = new HashMap<>();
        queries.put("hash", Collections.singletonList("454352394"));
        doReturn(queries).when(requestTemplate).queries();

        MarvelQueriesInfo marvelQueriesInfo = new MarvelQueriesInfo();
        marvelQueriesInfo.setStatusCode(200);
        marvelQueriesInfo.setRequestType("GET");
        marvelQueriesInfo.setResponseTime(new Date().getTime());


        when(marvelQueriesInfoRepository.findByHash("454352394")).thenReturn(marvelQueriesInfo);
        when(marvelQueriesInfoRepository.save(any(MarvelQueriesInfo.class))).thenReturn(marvelQueriesInfo);

        Object proceed = responseInterceptor.intercept(invocationContext, chain);

        Assertions.assertNotNull(proceed);
        verify(marvelQueriesInfoRepository, times(1)).findByHash(anyString());
        verify(marvelQueriesInfoRepository, times(1)).save(any(MarvelQueriesInfo.class));
    }
}
