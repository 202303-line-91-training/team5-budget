package budget

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Period(private val startDate: LocalDate, private val endDate: LocalDate) {

    fun overlappingDays(budget: Budget): Long {
        val overlappingStart: LocalDate = if (startDate.isAfter(budget.firstDay())) startDate else budget.firstDay()
        val overlappingEnd: LocalDate = if (endDate.isBefore(budget.lastDay())) endDate else budget.lastDay()
        return ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
    }
}
