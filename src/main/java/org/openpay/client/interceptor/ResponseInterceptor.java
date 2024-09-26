package org.openpay.client.interceptor;

import feign.InvocationContext;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.openpay.client.model.MarvelQueriesInfo;
import org.openpay.client.repository.MarvelQueriesInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ResponseInterceptor implements feign.ResponseInterceptor {

    private final MarvelQueriesInfoRepository marvelQueriesInfoRepository;

    @Autowired
    public ResponseInterceptor(MarvelQueriesInfoRepository marvelQueriesInfoRepository){
        this.marvelQueriesInfoRepository = marvelQueriesInfoRepository;
    }



    @Override
    public Object intercept(InvocationContext invocationContext, Chain chain) throws Exception {

        Response response = invocationContext.response();
        Request request = response.request();
        RequestTemplate requestTemplate = request.requestTemplate();

        List<String> hashItems = (List<String>) requestTemplate.queries().get("hash");
        if (!hashItems.isEmpty()) {
            String hash = hashItems.get(0);
            MarvelQueriesInfo marvelQueriesInfo = marvelQueriesInfoRepository.findByHash(hash);
            LocalDateTime now = LocalDateTime.now();
            marvelQueriesInfo.setResponseDateTime(now);
            marvelQueriesInfo.setInfo(response.reason());
            marvelQueriesInfo.setStatusCode(response.status());
            marvelQueriesInfo.setResponseTime(marvelQueriesInfo.getResponseDateTime().until(now, ChronoUnit.MILLIS));
            marvelQueriesInfoRepository.save(marvelQueriesInfo);
        }
        return invocationContext.proceed();
    }



}
