package com.saami.realestate;

import com.saami.realestate.enums.City;
import com.saami.realestate.model.Listing;
import com.saami.realestate.model.zillow.ZillowSearchResponse;
import com.saami.realestate.service.api.MLSDataService;
import com.saami.realestate.service.api.PropertyReportService;
import com.saami.realestate.service.api.ZillowService;
import com.saami.realestate.util.RealEstateCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RealestateApplicationTests {

	@Autowired
	ZillowService zillowService;

	@Autowired
	MLSDataService mlsDataService;

	@Autowired
	PropertyReportService propertyReportService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getSearchResultsTest(){
		String city = "Las Vegas";
		String state = "NV";
		String address = "3799 S Las Vegas Blvd";
		ZillowSearchResponse result = zillowService.getZillowSearchData(address, city, state);

		String saami = "saami";

	}

	@Test
	public void getDataFromMLS(){
		List<Listing> listings = mlsDataService.getMLSData();

		for(Listing listing : listings) {
			System.out.println(listing.getAddress().getStreet());
		}

		String saami = "saami";

	}

	@Test
	public void testMortgageCalculator() {
		double amount = RealEstateCalculator.calculateMonthlyPayment(90000);
		String saami = "saami";
	}

	@Test
	public void testTaxCalculator() {
		double amount = RealEstateCalculator.calculateAnnualTax(City.CHARLOTTE.getTaxRate(),250000d);
		String saami = "saami";
	}

	@Test
	public void testReportGenerator() {
		String filePath = propertyReportService.generateReport();
		String saami = "saami";
	}

}
