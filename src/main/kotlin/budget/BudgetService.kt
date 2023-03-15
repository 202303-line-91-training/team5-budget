package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        if (endDate.isBefore(startDate)) return 0.0
        val budgets = budgetRepo.getAll()

        val startYearMonth = YearMonth.from(startDate)
        var currentYearMonth = startYearMonth
        var amount = 0.00
        val endYearMonth = YearMonth.from(endDate)
        while (currentYearMonth <= endYearMonth) {
            val budget = budgets.find { it.getYearMonth() == currentYearMonth }
            if (budget != null) {
                val dailyAmount = budget.dailyAmount()
                if (startYearMonth == endYearMonth) {
                    return dailyAmount * (ChronoUnit.DAYS.between(startDate, endDate) + 1)
                }
                if (budget.getYearMonth() == startYearMonth) {
                    val overlappingEnd = budget.lastDay()
                    val overlappingStart = startDate
                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else if (budget.getYearMonth() == endYearMonth) {
                    val overlappingEnd = endDate
                    val overlappingStart = budget.firstDay()
                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else {
                    amount += budget.amount
                }
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }

}
