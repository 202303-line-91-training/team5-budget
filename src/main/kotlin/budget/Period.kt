package budget

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Period(private val startDate: LocalDate, private val endDate: LocalDate) {

    fun overlappingDays(budget: Budget): Long {
        val another = Period(budget.firstDay(), budget.lastDay())
        val overlappingStart: LocalDate = if (startDate.isAfter(another.startDate)) startDate else another.startDate
        val overlappingEnd: LocalDate = if (endDate.isBefore(another.endDate)) endDate else another.endDate
        return ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
    }
}
