package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
) {

    fun overlappingAmount(period: Period): Double {
        return dailyAmount() * period.overlappingDays(createPeriod())
    }

    private fun createPeriod() = Period(firstDay(), lastDay())

    private fun dailyAmount(): Double {
        return amount.toDouble() / getYearMonth().lengthOfMonth()
    }

    private fun getYearMonth(): YearMonth {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"))
    }

    private fun lastDay(): LocalDate {
        return getYearMonth().atEndOfMonth()
    }

    private fun firstDay(): LocalDate {
        return getYearMonth().atDay(1)
    }

}

