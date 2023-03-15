package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        val yearMonthBudgetMap: Map<YearMonth, Budget> = getRange(
            start = YearMonth.from(startDate),
            end = YearMonth.from(endDate)
        ).associateBy { it.getYearMonth() }
//        ).associateBy { YearMonth.of(it.getYearMonthDate().year, it.getYearMonthDate().monthValue) }

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
        if (end.isBefore(start)) return emptyList()
        val budgetList: List<Budget> = budgetRepo.getAll()

        return budgetList.filter {
            val yearMonth = it.getYearMonth()
            val compareToEnd = yearMonth.compareTo(end)
            val compareToStart = yearMonth.compareTo(start)
            (compareToStart >= 0) && (compareToEnd <= 0)
        }
    }

    fun getDayBudget(budget: Budget): Double {
        return budget.amount.toDouble() / YearMonth.of(budget.getYearMonth().year, budget.getYearMonth().monthValue)
            .lengthOfMonth()
    }

    fun YearMonth.toMyString(): String = this.toString().replace("-", "")
}
