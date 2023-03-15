package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        if (endDate.isBefore(startDate)) return 0.0
        val startYearMonth = YearMonth.from(startDate)
        val endYearMonth = YearMonth.from(endDate)
        val budgets = budgetRepo.getAll().filter {
            val yearMonth = it.getYearMonth()
            !(startYearMonth.isAfter(yearMonth) || endYearMonth
                .isBefore(yearMonth))
        }
        val yearMonthBudgetMap: Map<YearMonth, Budget> = budgets.associateBy { it.getYearMonth() }

//        var yearMonth = YearMonth.of(startDate.year, startDate.month)
//        val endYearMonth = YearMonth.of(endDate.year, endDate.month)
        val dayAmountMap = mutableMapOf<YearMonth, Double>()
        var currentYearMonth = startYearMonth;
        while (currentYearMonth <= endYearMonth) {
            val monthBudget = yearMonthBudgetMap[currentYearMonth] ?: Budget(currentYearMonth.toMyString())
            dayAmountMap[currentYearMonth] = getDayBudget(monthBudget)
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        val map = mutableMapOf<YearMonth, Int>()
        var date = startDate
        while (date <= endDate) {
            val keyMonth = YearMonth.of(date.year, date.month)
            if (map.containsKey(keyMonth)) {
                map[keyMonth] = map[keyMonth]!!.plus(1)
            } else {
                map[keyMonth] = 1
            }
            date = date.plusDays(1)
        }

        var amount = 0.00
        map.forEach { (yearMonth, day) ->
            amount += day * (dayAmountMap[yearMonth] ?: 0.00)
        }

        return amount
    }

    fun getRange(start: YearMonth, end: YearMonth): List<Budget> {
        val budgetList: List<Budget> = budgetRepo.getAll()

        return budgetList.filter {
            val yearMonth = it.getYearMonth()
            !(start.isAfter(yearMonth) || end.isBefore(yearMonth))
        }
    }

    fun getDayBudget(budget: Budget): Double {
        return budget.amount.toDouble() / YearMonth.of(budget.getYearMonth().year, budget.getYearMonth().monthValue)
            .lengthOfMonth()
    }

    fun YearMonth.toMyString(): String = this.toString().replace("-", "")
}
