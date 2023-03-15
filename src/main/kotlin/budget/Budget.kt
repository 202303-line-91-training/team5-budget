package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
) {

    fun createPeriod() = Period(firstDay(), lastDay())
    fun dailyAmount(): Double {
        return amount.toDouble() / getYearMonth().lengthOfMonth()
    }

     fun overlappingAmount(period: Period): Double {
         return dailyAmount() * period.overlappingDays(createPeriod())
     }
    fun getYearMonth(): YearMonth {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"))
    }

    fun lastDay(): LocalDate {
        return getYearMonth().atEndOfMonth()
    }

    fun firstDay(): LocalDate {
        return getYearMonth().atDay(1)
    }

}

