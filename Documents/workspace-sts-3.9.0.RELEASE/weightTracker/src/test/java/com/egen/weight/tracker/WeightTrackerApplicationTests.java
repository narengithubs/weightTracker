package com.egen.weight.tracker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.egen.weight.tracker.service.MetricService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeightTrackerApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MetricService metricService;

	@Test
	public void create() throws Exception {
		int value = 150;
		boolean create = true;
		String data = "{\"timeStamp\": \"" + String.valueOf(System.currentTimeMillis()) + "\", \"value\": \"" + value + "\"}";
		Mockito.when(metricService.create(Mockito.anyString(),Mockito.anyInt())).thenReturn(create);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
						.post("/create")
						.accept(MediaType.APPLICATION_JSON).content(data)
						.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK, response.getStatus());
	}
	
	@Test
	public void read() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders
						.post("/readMetrics")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK, response.getStatus());
	}
	
	@Test
	public void readMetricsByTimeRange() throws Exception {
		String timeRange = "{\"fromTimeStamp\": \"" + String.valueOf(System.currentTimeMillis()) + "\", \"toTimeStamp\": \"" + String.valueOf(System.currentTimeMillis()) + "\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders
						.post("/readMetricsByTimeRange")
						.accept(MediaType.APPLICATION_JSON).content(timeRange)
						.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK, response.getStatus());
	}


}
