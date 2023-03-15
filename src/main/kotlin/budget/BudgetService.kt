package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        if (endDate.isBefore(startDate)) return 0.0
        val budgets = budgetRepo.getAll().filter {
            val yearMonth = it.getYearMonth()
            !(YearMonth.from(startDate).isAfter(yearMonth) || YearMonth.from(endDate)
                .isBefore(yearMonth))
        }
        val yearMonthBudgetMap: Map<YearMonth, Budget> = budgets.associateBy { it.getYearMonth() }

        var yearMonth = YearMonth.of(startDate.year, startDate.month)
        val endYearMonth = YearMonth.of(endDate.year, endDate.month)
        val dayAmountMap = mutableMapOf<YearMonth, Double>()
        while (yearMonth <= endYearMonth) {
            val monthBudget = yearMonthBudgetMap[yearMonth] ?: Budget(yearMonth.toMyString())
            dayAmountMap[yearMonth] = getDayBudget(monthBudget)
            yearMonth = yearMonth.plusMonths(1)
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
