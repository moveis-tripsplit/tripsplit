package com.imd.tripsplit.data.remote

import com.imd.tripsplit.data.remote.dto.ActivityResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLPathPart
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import com.imd.tripsplit.data.remote.dto.ActivityRequestDto
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.request.put
import io.ktor.client.request.delete

class ActivityApiClient(
    private val client: HttpClient = HttpClientFactory.create(),
    private val baseUrl: String = "https://api.tripsplit.com",
) {
    suspend fun getActivities(tripId: String): Result<List<ActivityResponseDto>> {
        return try {
            val encodedTripId = tripId.encodeURLPathPart()
            val response = client.get(
                "${baseUrl.trimEnd('/')}/api/v1/trips/$encodedTripId/activities",
            )

            if (response.status.isSuccess()) {
                Result.success(response.body<List<ActivityResponseDto>>())
            } else {
                Result.failure(
                    IllegalStateException(
                        "Falha ao buscar atividades: HTTP ${response.status.value}",
                    ),
                )
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun createActivity(
        tripId: String,
        request: ActivityRequestDto,
    ): Result<ActivityResponseDto> {
        return try {
            val encodedTripId = tripId.encodeURLPathPart()
            val response = client.post(
                "${baseUrl.trimEnd('/')}/api/v1/trips/$encodedTripId/activities",
            ) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                Result.success(response.body<ActivityResponseDto>())
            } else {
                Result.failure(
                    IllegalStateException(
                        "Falha ao criar atividade: HTTP ${response.status.value}",
                    ),
                )
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun updateActivity(
        tripId: String,
        activityId: String,
        request: ActivityRequestDto,
    ): Result<ActivityResponseDto> {
        return try {
            val encodedTripId = tripId.encodeURLPathPart()
            val encodedActivityId = activityId.encodeURLPathPart()
            val response = client.put(
                "${baseUrl.trimEnd('/')}/api/v1/trips/$encodedTripId/activities/$encodedActivityId",
            ) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            if (response.status.isSuccess()) {
                Result.success(response.body<ActivityResponseDto>())
            } else {
                Result.failure(
                    IllegalStateException(
                        "Falha ao atualizar atividade: HTTP ${response.status.value}",
                    ),
                )
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    suspend fun deleteActivity(
        tripId: String,
        activityId: String,
    ): Result<Unit> {
        return try {
            val encodedTripId = tripId.encodeURLPathPart()
            val encodedActivityId = activityId.encodeURLPathPart()
            val response = client.delete(
                "${baseUrl.trimEnd('/')}/api/v1/trips/$encodedTripId/activities/$encodedActivityId",
            )

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(
                    IllegalStateException(
                        "Falha ao excluir atividade: HTTP ${response.status.value}",
                    ),
                )
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}