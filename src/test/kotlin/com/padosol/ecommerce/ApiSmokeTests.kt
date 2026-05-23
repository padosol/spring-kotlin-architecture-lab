package com.padosol.ecommerce

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ApiSmokeTests(
	@Autowired private val mockMvc: MockMvc,
) {

	@Test
	fun `payment api approves card payment`() {
		mockMvc.perform(
			post("/api/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"orderId": "order-1",
						"amount": 10000,
						"method": "CARD"
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.orderId").value("order-1"))
			.andExpect(jsonPath("$.method").value("CARD"))
			.andExpect(jsonPath("$.status").value("APPROVED"))
	}

	@Test
	fun `notification api sends email notification`() {
		mockMvc.perform(
			post("/api/notifications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"recipient": "user@example.com",
						"title": "Hello",
						"message": "Welcome",
						"channel": "EMAIL"
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.recipient").value("user@example.com"))
			.andExpect(jsonPath("$.channel").value("EMAIL"))
			.andExpect(jsonPath("$.status").value("SENT"))
	}

	@Test
	fun `auth api authenticates password login`() {
		mockMvc.perform(
			post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"principal": "user@example.com",
						"credential": "secret",
						"type": "PASSWORD"
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.principal").value("user@example.com"))
			.andExpect(jsonPath("$.type").value("PASSWORD"))
			.andExpect(jsonPath("$.authenticated").value(true))
	}
}
