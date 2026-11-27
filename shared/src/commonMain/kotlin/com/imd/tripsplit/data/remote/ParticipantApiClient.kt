package com.imd.tripsplit.data.remote

import com.imd.tripsplit.data.remote.dto.ParticipantRequestDto
import com.imd.tripsplit.data.remote.dto.ParticipantResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ParticipantApiClient(
    private val client: HttpClient = HttpClientFactory.create(),
    private val baseUrl: String = "https://api.tripsplit.com",
) {
    suspend fun getParticipants(tripId: String): Result<List<ParticipantResponseDto>> {
        return runCatching {
            val response = client.get("$baseUrl/trips/$tripId/participants")
            if (response.status.isSuccess()) {
                response.body<List<ParticipantResponseDto>>()
            } else {
                throw IllegalStateException("Falha ao buscar participantes: HTTP ${response.status.value}")
            }
        }
    }

    suspend fun createParticipant(
        tripId: String,
        request: ParticipantRequestDto,
    ): Result<ParticipantResponseDto> {
        return runCatching {
            val response =
                client.post("$baseUrl/trips/$tripId/participants") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            if (response.status.isSuccess()) {
                response.body<ParticipantResponseDto>()
            } else {
                throw IllegalStateException("Falha ao criar participante: HTTP ${response.status.value}")
            }
        }
    }

    suspend fun updateParticipant(
        tripId: String,
        participantId: String,
        request: ParticipantRequestDto,
    ): Result<ParticipantResponseDto> {
        return runCatching {
            val response =
                client.put("$baseUrl/trips/$tripId/participants/$participantId") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
            if (response.status.isSuccess()) {
                response.body<ParticipantResponseDto>()
            } else {
                throw IllegalStateException("Falha ao atualizar participante: HTTP ${response.status.value}")
            }
        }
    }

    suspend fun deleteParticipant(
        tripId: String,
        participantId: String,
    ): Result<Unit> {
        return runCatching {
            val response = client.delete("$baseUrl/trips/$tripId/participants/$participantId")
            if (response.status.isSuccess()) {
                // Sucesso
            } else {
                throw IllegalStateException("Falha ao remover participante: HTTP ${response.status.value}")
            }
        }
    }
}
