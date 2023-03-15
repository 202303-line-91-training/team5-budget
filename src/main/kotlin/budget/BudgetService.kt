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
                val overlappingDays =
                    Period(startDate, endDate).overlappingDays(budget.createPeriod())
                val overlappingAmount = budget.dailyAmount() * overlappingDays
                amount += overlappingAmount
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }

}
