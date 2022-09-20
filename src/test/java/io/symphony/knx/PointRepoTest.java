package io.symphony.knx;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PointRepoTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testCreateQuantityPoint() throws Exception {
		Map<String, Object> point = Map.of(
			"name", "office_temp",
			"labels", Map.of("room", "Office"),
			"address", "1/2/3",
			"stateAddress", "1/2/4",
			"unit", "CELSIUS",
			"value", 23.4,
			"type", "Quantity"
		);
		
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/points")
			.content(asJsonString(point))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
	      	.andExpect(status().isCreated());
	}
	
	@Test
	public void testCreateQuantityPointWithBadUnit() throws Exception {
		Map<String, Object> point = Map.of(
			"name", "office_temp",
			"labels", Map.of("room", "Office"),
			"address", "1/2/3",
			"stateAddress", "1/2/4",
			"unit", "FOOBAR",
			"value", 23.4,
			"type", "Quantity"
		);
		
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/points")
			.content(asJsonString(point))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
	      	.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateQuantityPointWithBadValue() throws Exception {
		Map<String, Object> point = Map.of(
			"name", "office_temp",
			"labels", Map.of("room", "Office"),
			"address", "1/2/3",
			"stateAddress", "1/2/4",
			"unit", "CELSIUS",
			"value", "foobar",
			"type", "Quantity"
		);
		
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/points")
			.content(asJsonString(point))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
	      	.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void testCreateSwitchPoint() throws Exception {
		Map<String, Object> point = Map.of(
			"name", "office_light",
			"labels", Map.of("room", "Office"),
			"address", "3/2/1",
			"stateAddress", "3/2/2",
			"state", "ON",
			"type", "Switch"
		);
		
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/points")
			.content(asJsonString(point))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
	      	.andExpect(status().isCreated());
	}
	
	@Test
	public void testCreateSwitchPointWithBadState() throws Exception {
		Map<String, Object> point = Map.of(
			"name", "office_light",
			"labels", Map.of("room", "Office"),
			"address", "3/2/1",
			"stateAddress", "3/2/2",
			"value", "DOWN",
			"type", "Switch"
		);
		
		this.mockMvc.perform(MockMvcRequestBuilders
			.post("/points")
			.content(asJsonString(point))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
	      	.andExpect(status().isBadRequest());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
}
