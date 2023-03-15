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
                val overlappingStart: LocalDate
                val overlappingEnd: LocalDate
                if (startYearMonth == endYearMonth) {
                    overlappingStart = startDate
                    overlappingEnd = endDate
                } else if (budget.getYearMonth() == startYearMonth) {
                    overlappingEnd = budget.lastDay()
                    overlappingStart = startDate
                } else if (budget.getYearMonth() == endYearMonth) {
                    overlappingEnd = endDate
                    overlappingStart = budget.firstDay()
                } else {
                    overlappingEnd = budget.lastDay()
                    overlappingStart = budget.firstDay()
                }
                val overlappingDays = ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
                amount += budget.dailyAmount() * overlappingDays
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }

}
