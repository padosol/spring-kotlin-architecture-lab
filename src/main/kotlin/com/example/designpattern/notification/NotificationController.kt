package com.example.designpattern.notification

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

enum class NotificationChannel {
	EMAIL,
	SMS,
	PUSH,
}

enum class NotificationStatus {
	SENT,
}

data class NotificationRequest(
	@field:NotBlank
	val recipient: String,
	@field:NotBlank
	val title: String,
	@field:NotBlank
	val message: String,
	val channel: NotificationChannel,
)

data class NotificationResponse(
	val notificationId: String,
	val recipient: String,
	val channel: NotificationChannel,
	val status: NotificationStatus,
)

interface NotificationSender {
	fun supports(channel: NotificationChannel): Boolean
	fun send(request: NotificationRequest): NotificationResponse
}

@Service
class EmailNotificationSender : NotificationSender {
	override fun supports(channel: NotificationChannel): Boolean = channel == NotificationChannel.EMAIL

	override fun send(request: NotificationRequest): NotificationResponse =
		NotificationResponse(
			notificationId = "email-${UUID.randomUUID()}",
			recipient = request.recipient,
			channel = request.channel,
			status = NotificationStatus.SENT,
		)
}

@Service
class SmsNotificationSender : NotificationSender {
	override fun supports(channel: NotificationChannel): Boolean = channel == NotificationChannel.SMS

	override fun send(request: NotificationRequest): NotificationResponse =
		NotificationResponse(
			notificationId = "sms-${UUID.randomUUID()}",
			recipient = request.recipient,
			channel = request.channel,
			status = NotificationStatus.SENT,
		)
}

@Service
class PushNotificationSender : NotificationSender {
	override fun supports(channel: NotificationChannel): Boolean = channel == NotificationChannel.PUSH

	override fun send(request: NotificationRequest): NotificationResponse =
		NotificationResponse(
			notificationId = "push-${UUID.randomUUID()}",
			recipient = request.recipient,
			channel = request.channel,
			status = NotificationStatus.SENT,
		)
}

@Service
class NotificationService(
	private val senders: List<NotificationSender>,
) {
	fun send(request: NotificationRequest): NotificationResponse {
		val sender = senders.firstOrNull { it.supports(request.channel) }
			?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported notification channel: ${request.channel}")

		return sender.send(request)
	}
}

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
	private val notificationService: NotificationService,
) {
	@PostMapping
	fun send(@Valid @RequestBody request: NotificationRequest): NotificationResponse =
		notificationService.send(request)
}
