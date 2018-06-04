package com.saami.realestate.service.impl;

import com.saami.realestate.model.zillow.DeepCompsResponse;
import com.saami.realestate.model.zillow.UpdatedDetailsResponse;
import com.saami.realestate.model.zillow.ZestimateResonse;
import com.saami.realestate.model.zillow.ZillowSearchResponse;
import com.saami.realestate.service.api.ZillowService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sasiddi on 5/1/17.
 */
@Component
@ConfigurationProperties("application.properties")
public class ZillowServiceImpl implements ZillowService {
    private static final Logger LOG = LoggerFactory.getLogger(ZillowServiceImpl.class);

    private static final String ID_PARAM = "zws-id" ;
    private static final String ADDRESS_PARAM = "address";
    private static final String CITY_PARAM = "citystatezip";
    private static final String RENT_PARAM = "rentzestimate";
    private static final String ZP_ID = "zpid";
    private static final String COUNT = "count";

    @Value("${api.zillow.id}")
    String id;

    @Value("${api.zillow.search.url}")
    String searchUrl;

    @Value("${api.zillow.zestimate.url}")
    String zestimateUrl;

    @Value("${api.zillow.comps.url}")
    String deepCompsUrl;

    @Value("${api.zillow.updated.property.details.url}")
    String updatedDetailsUrl;

    @Override
    public ZillowSearchResponse getZillowSearchData(String street, String city, String state) {
        if (StringUtils.isEmpty(street) || StringUtils.isEmpty(city) || StringUtils.isEmpty(state)) {
            throw new IllegalArgumentException("Street, City and State are required for Zillow getZillowSearchData api");
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(ID_PARAM, id);
        paramMap.put(ADDRESS_PARAM, encodeString(street));
        paramMap.put(CITY_PARAM, encodeString(city + ", " + state));
        paramMap.put(RENT_PARAM, "true");
        final String urlWithParams = appendParams(searchUrl, paramMap);
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClientBuilder.create().build();
            httpGet = new HttpGet(urlWithParams);
            httpGet.addHeader("content-type", "application/xml");

            LOG.debug("Hitting Zillow search results api: " + urlWithParams);
            response = httpClient.execute(httpGet);

            final int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus < 200 || responseStatus > 300) {
                LOG.error("Request Code", responseStatus);
                LOG.error("Request Message", response.getStatusLine());
                throw new Exception(String.format("Call to Zillow search results api did not return 200. Status:[%s]", responseStatus));
            }
            LOG.debug("Zillow search results api call successful. Status Code: " + responseStatus);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            return new ZillowSearchResponse(XML.toJSONObject(result.toString()));

        } catch (Exception e) {
            LOG.error("Error getting from Zillow search results: " + urlWithParams, e);
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    @Override
    public ZestimateResonse getZestimateData(String zpId) {
        if (StringUtils.isEmpty(zpId)) {
            throw new IllegalArgumentException("zpId is required for Zillow getZestimate api");
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(ID_PARAM, id);
        paramMap.put(ZP_ID, zpId);
        paramMap.put(RENT_PARAM, "true");
        final String urlWithParams = appendParams(zestimateUrl, paramMap);
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClientBuilder.create().build();
            httpGet = new HttpGet(urlWithParams);
            httpGet.addHeader("content-type", "application/xml");

            LOG.debug("Hitting Zillow Zestimate api: " + urlWithParams);
            response = httpClient.execute(httpGet);

            final int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus < 200 || responseStatus > 300) {
                LOG.error("Request Code", responseStatus);
                LOG.error("Request Message", response.getStatusLine());
                throw new Exception(String.format("Call to Zillow search results api did not return 200. Status:[%s]", responseStatus));
            }
            LOG.debug("Zillow zestimate api call successful. Status Code: " + responseStatus);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            return new ZestimateResonse(XML.toJSONObject(result.toString()));

        } catch (Exception e) {
            LOG.error("Error getting from Zillow zestimat api: " + urlWithParams, e);
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    @Override
    public UpdatedDetailsResponse getUpdatedDetails(String zpId) {
        if (StringUtils.isEmpty(zpId)) {
            throw new IllegalArgumentException("zpId is required for Zillow getUpdatedDetails api");
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(ID_PARAM, id);
        paramMap.put(ZP_ID, zpId);
        final String urlWithParams = appendParams(updatedDetailsUrl, paramMap);
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClientBuilder.create().build();
            httpGet = new HttpGet(urlWithParams);
            httpGet.addHeader("content-type", "application/xml");

            LOG.debug("Hitting Zillow deepComps api: " + urlWithParams);
            response = httpClient.execute(httpGet);

            final int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus < 200 || responseStatus > 300) {
                LOG.error("Request Code", responseStatus);
                LOG.error("Request Message", response.getStatusLine());
                throw new Exception(String.format("Call to Zillow get updated results api did not return 200. Status:[%s]", responseStatus));
            }
            LOG.debug("Zillow get updated results api call successful. Status Code: " + responseStatus);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            return new UpdatedDetailsResponse(XML.toJSONObject(result.toString()));

        } catch (Exception e) {
            LOG.error("Error getting from Zillow zestimat api: " + urlWithParams, e);
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    @Override
    public DeepCompsResponse getDeepComps(String zpId) {
        if (StringUtils.isEmpty(zpId)) {
            throw new IllegalArgumentException("zpId is required for Zillow getDeepComps api");
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(ID_PARAM, id);
        paramMap.put(ZP_ID, zpId);
        paramMap.put(RENT_PARAM, "true");
        paramMap.put(COUNT, "5");
        final String urlWithParams = appendParams(deepCompsUrl, paramMap);
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClientBuilder.create().build();
            httpGet = new HttpGet(urlWithParams);
            httpGet.addHeader("content-type", "application/xml");

            LOG.debug("Hitting Zillow deepComps api: " + urlWithParams);
            response = httpClient.execute(httpGet);

            final int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus < 200 || responseStatus > 300) {
                LOG.error("Request Code", responseStatus);
                LOG.error("Request Message", response.getStatusLine());
                throw new Exception(String.format("Call to Zillow search results api did not return 200. Status:[%s]", responseStatus));
            }
            LOG.debug("Zillow deepComps api call successful. Status Code: " + responseStatus);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            return new DeepCompsResponse(XML.toJSONObject(result.toString()));

        } catch (Exception e) {
            LOG.error("Error getting from Zillow zestimat api: " + urlWithParams, e);
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder urlWithParams = new StringBuilder(url);
        urlWithParams.append("?");

        boolean firstParam = true;
        for (String param : params.keySet()) {
            if(!firstParam) {
                urlWithParams.append("&");
            }
            firstParam = false;
            urlWithParams.append(param);
            urlWithParams.append("=");
            urlWithParams.append(params.get(param));
        }

        return urlWithParams.toString();

    }

    private String encodeString(String param) {
        if (param == null) {
            throw new IllegalArgumentException("param to encode can not be null");
        }
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("error encoding pram: " + param, e);
            return null;
        }
    }

}
