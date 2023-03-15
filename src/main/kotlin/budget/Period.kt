package budget

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Period(private val startDate: LocalDate, private val endDate: LocalDate) {

    fun overlappingDays(budget: Budget): Long {
        val another = Period(budget.firstDay(), budget.lastDay())
        val firstDay = another.startDate
        val lastDay = another.endDate
        val overlappingStart: LocalDate = if (startDate.isAfter(firstDay)) startDate else firstDay
        val overlappingEnd: LocalDate = if (endDate.isBefore(lastDay)) endDate else lastDay
        return ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
    }
}
