package com.imd.tripsplit

import com.imd.tripsplit.data.remote.ActivityApiClient
import com.imd.tripsplit.data.remote.HttpClientFactory
import com.imd.tripsplit.data.remote.dto.ActivityRequestDto
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActivityApiClientTest {
    @Test
    fun getActivitiesBuscaRoteiroDaViagemEDecodificaParticipantes() =
        runTest {
            val engine =
                MockEngine { request ->
                    assertEquals(HttpMethod.Get, request.method)
                    assertEquals("/api/v1/trips/trip-42/activities", request.url.encodedPath)

                    respond(
                        content =
                            """
                            [
                              {
                                "id": "atividade-1",
                                "title": "Passeio de barco",
                                "dateHour": "29/11/2026 • 14:30",
                                "location": "Ilha Grande",
                                "estimatedCost": 19.90,
                                "participantIds": ["ana", "bia"]
                              }
                            ]
                            """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                    )
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")
                val activity = api.getActivities("trip-42").getOrThrow().single()

                assertEquals("atividade-1", activity.id)
                assertEquals("Passeio de barco", activity.title)
                assertEquals("29/11/2026 • 14:30", activity.dateHour)
                assertEquals("Ilha Grande", activity.location)
                assertEquals(19.90, activity.estimatedCost)
                assertEquals(listOf("ana", "bia"), activity.participantIds)
            } finally {
                client.close()
            }
        }

    @Test
    fun getActivitiesAceitaRoteiroVazio() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content = "[]",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                    )
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")

                assertTrue(api.getActivities("trip-42").getOrThrow().isEmpty())
            } finally {
                client.close()
            }
        }

    @Test
    fun getActivitiesRetornaFalhaQuandoServidorResponde503() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content = "Serviço indisponível",
                        status = HttpStatusCode.ServiceUnavailable,
                    )
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")
                val result = api.getActivities("trip-42")

                assertTrue(result.isFailure)
                assertTrue(result.exceptionOrNull()?.message.orEmpty().contains("503"))
            } finally {
                client.close()
            }
        }

    @Test
    fun createActivityEnviaDadosEViajantesERetornaIdDoServidor() =
        runTest {
            val requestDto =
                ActivityRequestDto(
                    title = "Passeio de barco",
                    dateHour = "29/11/2026 • 14:30",
                    location = "Ilha Grande",
                    estimatedCost = 19.90,
                    participantIds = listOf("ana", "bia"),
                )

            val engine =
                MockEngine { request ->
                    assertEquals(HttpMethod.Post, request.method)
                    assertEquals("/api/v1/trips/trip-42/activities", request.url.encodedPath)

                    val body = request.body as TextContent
                    assertEquals(
                        ContentType.Application.Json,
                        body.contentType.withoutParameters(),
                    )
                    assertEquals(
                        requestDto,
                        Json.decodeFromString(ActivityRequestDto.serializer(), body.text),
                    )

                    respond(
                        content =
                            """
                            {
                              "id": "atividade-123",
                              "title": "Passeio de barco",
                              "dateHour": "29/11/2026 • 14:30",
                              "location": "Ilha Grande",
                              "estimatedCost": 19.90,
                              "participantIds": ["ana", "bia"]
                            }
                            """.trimIndent(),
                        status = HttpStatusCode.Created,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")
                val created = api.createActivity("trip-42", requestDto).getOrThrow()

                assertEquals("atividade-123", created.id)
                assertEquals(listOf("ana", "bia"), created.participantIds)
            } finally {
                client.close()
            }
        }

    @Test
    fun updateActivityEnviaDadosEditadosEPermiteRemoverTodosOsViajantes() =
        runTest {
            val edited =
                ActivityRequestDto(
                    title = "Passeio de barco à tarde",
                    dateHour = "29/11/2026 • 15:30",
                    location = "Cais principal",
                    estimatedCost = 0.0,
                    participantIds = emptyList(),
                )

            val engine =
                MockEngine { request ->
                    assertEquals(HttpMethod.Put, request.method)
                    assertEquals(
                        "/api/v1/trips/trip-42/activities/atividade-123",
                        request.url.encodedPath,
                    )

                    val body = request.body as TextContent
                    assertEquals(
                        edited,
                        Json.decodeFromString(ActivityRequestDto.serializer(), body.text),
                    )

                    respond(
                        content =
                            """
                            {
                              "id": "atividade-123",
                              "title": "Passeio de barco à tarde",
                              "dateHour": "29/11/2026 • 15:30",
                              "location": "Cais principal",
                              "estimatedCost": 0.0,
                              "participantIds": []
                            }
                            """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")
                val updated = api.updateActivity("trip-42", "atividade-123", edited).getOrThrow()

                assertEquals("atividade-123", updated.id)
                assertEquals("Passeio de barco à tarde", updated.title)
                assertEquals("Cais principal", updated.location)
                assertEquals("29/11/2026 • 15:30", updated.dateHour)
                assertEquals(0.0, updated.estimatedCost)
                assertTrue(updated.participantIds.isEmpty())
            } finally {
                client.close()
            }
        }

    @Test
    fun deleteActivityUsaOsIdsCorretosEAceitaRespostaSemCorpo() =
        runTest {
            val engine =
                MockEngine { request ->
                    assertEquals(HttpMethod.Delete, request.method)
                    assertEquals(
                        "/api/v1/trips/trip-42/activities/atividade-123",
                        request.url.encodedPath,
                    )

                    respond(content = "", status = HttpStatusCode.NoContent)
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")

                api.deleteActivity("trip-42", "atividade-123").getOrThrow()
            } finally {
                client.close()
            }
        }

    @Test
    fun deleteActivityRetornaFalhaQuandoServidorRecusaExclusao() =
        runTest {
            val engine =
                MockEngine {
                    respond(content = "", status = HttpStatusCode.Forbidden)
                }

            val client = HttpClientFactory.create(engine)
            try {
                val api = ActivityApiClient(client, baseUrl = "https://tripsplit.test")
                val result = api.deleteActivity("trip-42", "atividade-123")

                assertTrue(result.isFailure)
                assertTrue(result.exceptionOrNull()?.message.orEmpty().contains("403"))
            } finally {
                client.close()
            }
        }
}
