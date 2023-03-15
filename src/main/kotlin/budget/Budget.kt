package budget

import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
) {

    fun dailyAmount(): Double {
        return amount.toDouble() / getYearMonth().lengthOfMonth()
    }

    fun getYearMonth(): YearMonth {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    }

}

