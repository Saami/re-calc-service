package com.saami.realestate.service.impl;

import com.saami.realestate.model.Address;
import com.saami.realestate.model.Listing;
import com.saami.realestate.service.api.MLSDataService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sasiddi on 5/3/17.
 */
@Component
public class CharlotteMLSServiceImpl implements MLSDataService{
    private static final Logger LOG = LoggerFactory.getLogger(CharlotteMLSServiceImpl.class);
    private static final String TABLE_CLASS = "table.d5m2";
    private static final String DATE_CLASS = "table.d5m6";
    private static final String STATUS_CLASS = "td.d5m10";
    private static final String ADDRESS_CLASS = "td.d5m13";
    private static final String CITY_CLASS = "td.d5m14";
    private static final String PRICE_CLASS = "td.d5m17";
    private static final String STATE = "NC";

    @Override
    public List<Listing> getMLSData() {
        return getDataFromPage();

    }

    private List<Listing> getDataFromPage() {
        List<Listing> listings = new ArrayList<>();
        try {
            URL url = new URL("http://matrix.carolinamls.com/Matrix/Public/Portal.aspx?L=1&k=1107962XH8TQ&p=ALL-0-0-H");
            Document doc = Jsoup.parse(url, 5000);

            Elements elements = doc.select(TABLE_CLASS);

            for (Element table : doc.select(TABLE_CLASS)) {
                for (Element row : table.select("tr")) {
                    String date = row.select(DATE_CLASS).text();
                    String city = row.select(CITY_CLASS).text();
                    String houseAddress = row.select(ADDRESS_CLASS).text();
                    String status = row.select(STATUS_CLASS).text();
                    Double price = Double.valueOf(row.select(PRICE_CLASS).text().replaceAll("[^\\d.]+", ""));

                    Address address = new Address()
                            .setCity(city)
                            .setState(STATE)
                            .setAddress(houseAddress);

                    Listing listing = new Listing()
                            .setDate(date)
                            .setStatus(status)
                            .setPrice(price)
                            .setAddress(address);

                    listings.add(listing);

                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return listings;
    }
}
