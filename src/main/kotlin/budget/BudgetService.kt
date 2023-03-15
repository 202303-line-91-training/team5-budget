package budget

import java.time.Duration.between
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        if (endDate.isBefore(startDate)) return 0.0
        val budgets = budgetRepo.getAll()

        val dayAmountMap = mutableMapOf<YearMonth, Double>()
        val startYearMonth = YearMonth.from(startDate)
        var currentYearMonth = startYearMonth
        var amount = 0.00
        val endYearMonth = YearMonth.from(endDate)
        while (currentYearMonth <= endYearMonth) {
            val budget = budgets.find { it.getYearMonth() == currentYearMonth }
            if (budget != null) {
                if (startYearMonth == endYearMonth) {
                    return budget.dailyAmount() * (ChronoUnit.DAYS.between(startDate, endDate) + 1)
                }
                if (budget.getYearMonth() == startYearMonth) {
                    val overlappingEnd = budget.lastDay()
                    val overlappingStart = startDate
                    val dailyAmount = budget.dailyAmount()
                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else if (budget.getYearMonth() == endYearMonth) {
                    val overlappingEnd = endDate
                    val overlappingStart = budget.firstDay()
                    val dailyAmount = budget.dailyAmount()
                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else {
                    amount += budget.amount
                }
//                dayAmountMap[currentYearMonth] = dailyAmount
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
//        val map = mutableMapOf<YearMonth, Int>()
//        var date = startDate
//        while (date <= endDate) {
//            val keyMonth = YearMonth.of(date.year, date.month)
//            if (map.containsKey(keyMonth)) {
//                map[keyMonth] = map[keyMonth]!!.plus(1)
//            } else {
//                map[keyMonth] = 1
//            }
//            date = date.plusDays(1)
//        }
//
//        map.forEach { (yearMonth, day) ->
//            amount += day * (dayAmountMap[yearMonth] ?: 0.00)
//        }
//
//        return amount
    }

}
