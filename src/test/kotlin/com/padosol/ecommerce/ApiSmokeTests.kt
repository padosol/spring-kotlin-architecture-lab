package com.padosol.ecommerce

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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

	@Test
	fun `customer api returns customer baseline`() {
		mockMvc.perform(get("/api/customers/customer-1"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.customerId").value("customer-1"))
			.andExpect(jsonPath("$.status").value("ACTIVE"))
	}

	@Test
	fun `catalog api lists products`() {
		mockMvc.perform(get("/api/catalog/products"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$[0].productId").value("product-1"))
			.andExpect(jsonPath("$[0].status").value("ON_SALE"))
	}

	@Test
	fun `cart api previews cart total`() {
		mockMvc.perform(
			post("/api/carts/preview")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"customerId": "customer-1",
						"items": [
							{
								"productId": "product-1",
								"quantity": 2,
								"unitPrice": 12000
							}
						]
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.customerId").value("customer-1"))
			.andExpect(jsonPath("$.totalAmount").value(24000))
	}

	@Test
	fun `order api creates payment pending order`() {
		mockMvc.perform(
			post("/api/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"customerId": "customer-1",
						"items": [
							{
								"productId": "product-1",
								"quantity": 2,
								"unitPrice": 12000
							}
						]
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.customerId").value("customer-1"))
			.andExpect(jsonPath("$.status").value("PAYMENT_PENDING"))
			.andExpect(jsonPath("$.totalAmount").value(24000))
	}

	@Test
	fun `inventory api reserves product quantity`() {
		mockMvc.perform(
			post("/api/inventory/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"productId": "product-1",
						"quantity": 2
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.productId").value("product-1"))
			.andExpect(jsonPath("$.status").value("RESERVED"))
	}

	@Test
	fun `shipment api creates shipment baseline`() {
		mockMvc.perform(
			post("/api/shipments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"orderId": "order-1",
						"recipientName": "Test User",
						"address": "Seoul"
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.orderId").value("order-1"))
			.andExpect(jsonPath("$.status").value("READY"))
	}

	@Test
	fun `promotion api applies welcome coupon`() {
		mockMvc.perform(
			post("/api/promotions/apply")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
					"""
					{
						"customerId": "customer-1",
						"orderAmount": 10000,
						"couponCode": "WELCOME10"
					}
					""".trimIndent(),
				),
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.customerId").value("customer-1"))
			.andExpect(jsonPath("$.discountAmount").value(1000))
			.andExpect(jsonPath("$.payableAmount").value(9000))
	}
}
