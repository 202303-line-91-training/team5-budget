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
                val overlappingStart: LocalDate
                val overlappingEnd: LocalDate
                if (startYearMonth == endYearMonth) {
                    overlappingStart = startDate
                    overlappingEnd = endDate
//                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else if (budget.getYearMonth() == startYearMonth) {
                    overlappingEnd = budget.lastDay()
                    overlappingStart = startDate
//                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else if (budget.getYearMonth() == endYearMonth) {
                    overlappingEnd = endDate
                    overlappingStart = budget.firstDay()
//                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                } else {
                    overlappingEnd = budget.lastDay()
                    overlappingStart = budget.firstDay()
//                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
                }
                amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }

}
