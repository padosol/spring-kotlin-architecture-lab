package com.example.designpattern.auth

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

enum class AuthType {
	PASSWORD,
	OAUTH,
}

data class LoginRequest(
	@field:NotBlank
	val principal: String,
	@field:NotBlank
	val credential: String,
	val type: AuthType = AuthType.PASSWORD,
)

data class LoginResponse(
	val principal: String,
	val token: String,
	val type: AuthType,
	val authenticated: Boolean,
)

interface Authenticator {
	fun supports(type: AuthType): Boolean
	fun authenticate(request: LoginRequest): LoginResponse
}

@Service
class PasswordAuthenticator : Authenticator {
	override fun supports(type: AuthType): Boolean = type == AuthType.PASSWORD

	override fun authenticate(request: LoginRequest): LoginResponse =
		LoginResponse(
			principal = request.principal,
			token = "password-${UUID.randomUUID()}",
			type = request.type,
			authenticated = true,
		)
}

@Service
class OAuthAuthenticator : Authenticator {
	override fun supports(type: AuthType): Boolean = type == AuthType.OAUTH

	override fun authenticate(request: LoginRequest): LoginResponse =
		LoginResponse(
			principal = request.principal,
			token = "oauth-${UUID.randomUUID()}",
			type = request.type,
			authenticated = true,
		)
}

@Service
class AuthService(
	private val authenticators: List<Authenticator>,
) {
	fun login(request: LoginRequest): LoginResponse {
		val authenticator = authenticators.firstOrNull { it.supports(request.type) }
			?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported auth type: ${request.type}")

		return authenticator.authenticate(request)
	}
}

@RestController
@RequestMapping("/api/auth")
class AuthController(
	private val authService: AuthService,
) {
	@PostMapping("/login")
	fun login(@Valid @RequestBody request: LoginRequest): LoginResponse =
		authService.login(request)
}
