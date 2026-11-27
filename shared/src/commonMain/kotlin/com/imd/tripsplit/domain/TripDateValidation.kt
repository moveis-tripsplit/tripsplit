package com.imd.tripsplit.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

private val tripDatePattern = Regex("""(\d{2})/(\d{2})/(\d{4})""")
private val activityTimePattern = Regex("""(\d{2}):(\d{2})""")

private fun parseTripDate(text: String): LocalDate? {
    val match = tripDatePattern.matchEntire(text.trim()) ?: return null
    val (day, month, year) = match.destructured

    return try {
        LocalDate(year.toInt(), month.toInt(), day.toInt())
    } catch (_: IllegalArgumentException) {
        null
    }
}

private fun parseActivityTime(text: String): LocalTime? {
    val match = activityTimePattern.matchEntire(text.trim()) ?: return null
    val (hour, minute) = match.destructured

    return try {
        LocalTime(hour.toInt(), minute.toInt())
    } catch (_: IllegalArgumentException) {
        null
    }
}

internal fun tripDateRange(
    startDate: String,
    endDate: String,
): ClosedRange<LocalDate>? {
    val start = parseTripDate(startDate) ?: return null
    val end = parseTripDate(endDate) ?: return null
    return if (start <= end) start..end else null
}

internal fun activityDateError(
    dateHour: String,
    tripPeriod: ClosedRange<LocalDate>?,
): String? {
    if (tripPeriod == null) {
        return "Informe um período válido para a viagem antes de salvar a atividade."
    }

    val parts = dateHour.split('•')
    if (parts.size != 2) {
        return "Use o formato DD/MM/AAAA • HH:mm."
    }

    val date = parseTripDate(parts[0])
        ?: return "Informe uma data válida com dia, mês e ano."

    if (parseActivityTime(parts[1]) == null) {
        return "Informe um horário válido entre 00:00 e 23:59."
    }

    if (date !in tripPeriod) {
        return "A atividade deve acontecer dentro do período da viagem."
    }

    return null
}