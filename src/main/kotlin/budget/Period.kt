package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class Period(val startDate: LocalDate, val endDate: LocalDate) {

    fun overlappingDays(budget: Budget): Long {
        val overlappingStart: LocalDate = if (startDate.isAfter(budget.firstDay())) startDate else budget.firstDay()
        val overlappingEnd: LocalDate = if (endDate.isBefore(budget.lastDay())) endDate else budget.lastDay()
        if (budget.getYearMonth() == YearMonth.from(startDate)) {
//            overlappingEnd = budget.lastDay()
//            overlappingStart = startDate
        } else if (budget.getYearMonth() == YearMonth.from(endDate)) {
//            overlappingEnd = endDate
//            overlappingStart = budget.firstDay()
        } else {
//            overlappingEnd = budget.lastDay()
//            overlappingStart = budget.firstDay()
        }
        return ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
    }
}
