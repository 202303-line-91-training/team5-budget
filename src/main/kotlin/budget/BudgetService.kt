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

        var currentYearMonth = YearMonth.from(startDate)
        var amount = 0.00
        if (YearMonth.from(startDate) == YearMonth.from(endDate)) {
            val overlappingStart = startDate
            val overlappingEnd = endDate
            val overlappingDays = ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
            val budget = budgets.find { it.getYearMonth() == currentYearMonth }
            if (budget != null) {
                return budget.dailyAmount() * overlappingDays
            }
        }
        while (currentYearMonth <= YearMonth.from(endDate)) {
            val budget = budgets.find { it.getYearMonth() == currentYearMonth }
            if (budget != null) {
                val overlappingStart: LocalDate
                val overlappingEnd: LocalDate
                if (budget.getYearMonth() == YearMonth.from(startDate)) {
                    overlappingEnd = budget.lastDay()
                    overlappingStart = startDate
                } else if (budget.getYearMonth() == YearMonth.from(endDate)) {
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
