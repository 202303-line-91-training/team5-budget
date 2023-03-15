package budget

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Period(private val startDate: LocalDate, private val endDate: LocalDate) {

    fun overlappingDays(another: Period): Long {
        if (startDate.isAfter(another.endDate) || endDate.isBefore(another.startDate)) {
            return 0
        }
        val overlappingStart: LocalDate = if (startDate.isAfter(another.startDate)) startDate else another.startDate
        val overlappingEnd: LocalDate = if (endDate.isBefore(another.endDate)) endDate else another.endDate
        return ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
    }
}
