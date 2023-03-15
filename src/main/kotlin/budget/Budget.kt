package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
) {

    fun dailyAmount(): Double {
        return amount.toDouble() / YearMonth.of(getYearMonth().year, getYearMonth().monthValue)
            .lengthOfMonth()
    }

    fun getYearMonthDate(): LocalDate {
        return getYearMonth().atDay(1)
    }

    fun getYearMonth(): YearMonth {
        val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("uuuuMM")
            .toFormatter(Locale.ENGLISH)

        return YearMonth.parse(yearMonth, formatter)
    }

}

