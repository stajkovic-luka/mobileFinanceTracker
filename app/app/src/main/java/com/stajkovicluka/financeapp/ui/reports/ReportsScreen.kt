package com.stajkovicluka.financeapp.ui.reports

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.data.model.DailyDepositTotal
import com.stajkovicluka.financeapp.data.model.DepositReportItem
import com.stajkovicluka.financeapp.data.model.DepositReportResponse
import com.stajkovicluka.financeapp.data.model.MonthlyDepositTotal
import com.stajkovicluka.financeapp.util.ReportPdfExporter
import com.stajkovicluka.financeapp.viewmodel.ReportViewModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import java.util.Calendar
import java.math.BigDecimal

// Omogucava izbor perioda i prikaz izvestaja uplata.
@Composable
fun ReportsScreen(
    reportViewModel: ReportViewModel,
    modifier: Modifier = Modifier
) {
    var fromForRequest by rememberSaveable { mutableStateOf("") }
    var toForRequest by rememberSaveable { mutableStateOf("") }
    var fromForDisplay by rememberSaveable { mutableStateOf("") }
    var toForDisplay by rememberSaveable { mutableStateOf("") }
    var exportError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val exportErrorText = stringResource(R.string.report_export_error)
    val calendar = remember { Calendar.getInstance() }

    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.reports_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                OutlinedTextField(
                    value = fromForDisplay,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.report_from_date_label)) },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        TextButton(onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    fromForDisplay = formatDateForDisplay(dayOfMonth, month + 1, year)
                                    fromForRequest = formatDateForRequest(dayOfMonth, month + 1, year)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) {
                            Text(stringResource(R.string.select_date_button))
                        }
                    }
                )
            }
            item {
                OutlinedTextField(
                    value = toForDisplay,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.report_to_date_label)) },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        TextButton(onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    toForDisplay = formatDateForDisplay(dayOfMonth, month + 1, year)
                                    toForRequest = formatDateForRequest(dayOfMonth, month + 1, year)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }) {
                            Text(stringResource(R.string.select_date_button))
                        }
                    }
                )
            }
            item {
                Button(
                    onClick = { reportViewModel.loadReport(fromForRequest, toForRequest) },
                    enabled = !reportViewModel.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (reportViewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(stringResource(R.string.show_report_button))
                    }
                }
            }
            reportViewModel.errorMessage?.let { message ->
                item {
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                }
            }
            reportViewModel.report?.let { report ->
                item { ReportSummary(report, reportViewModel.averagePerGoal) }
                item {
                    Button(
                        onClick = {
                            try {
                                ReportPdfExporter.exportAndShare(
                                    context = context,
                                    report = report,
                                    averagePerGoal = reportViewModel.averagePerGoal,
                                    dailyTotals = reportViewModel.dailyTotals,
                                    monthlyTotals = reportViewModel.monthlyTotals
                                )
                                exportError = null
                            } catch (exception: Exception) {
                                exportError = exportErrorText
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.report_export_pdf_button))
                    }
                }
                exportError?.let { message ->
                    item {
                        Text(text = message, color = MaterialTheme.colorScheme.error)
                    }
                }
                if (reportViewModel.dailyTotals.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.report_trend_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item { DailyTotalsChart(reportViewModel.dailyTotals) }
                }
                if (reportViewModel.monthlyTotals.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(
                                R.string.report_monthly_trend_title,
                                reportViewModel.monthlyTotals.first().month.take(4)
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item { MonthlyTotalsChart(reportViewModel.monthlyTotals) }
                }
                item {
                    Text(
                        text = stringResource(R.string.report_deposits_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (report.deposits.isEmpty()) {
                    item { Text(stringResource(R.string.report_deposits_empty)) }
                } else {
                    items(report.deposits, key = { it.depositId }) { deposit ->
                        ReportDepositCard(deposit)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportSummary(report: DepositReportResponse, averagePerGoal: BigDecimal) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${formatDate(report.from)} - ${formatDate(report.to)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.report_total_deposited, report.totalDeposited.toPlainString()),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = stringResource(R.string.report_average_per_goal, averagePerGoal.toPlainString()),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun DailyTotalsChart(dailyTotals: List<DailyDepositTotal>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val dateFormatter = remember(dailyTotals) {
        CartesianValueFormatter { _, value, _ ->
            dailyTotals.getOrNull(value.toInt())?.date?.let(::formatDate).orEmpty()
        }
    }

    LaunchedEffect(dailyTotals) {
        modelProducer.runTransaction {
            columnModel {
                series(dailyTotals.map { it.totalAmount.toDouble() })
            }
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        ProvideVicoTheme(rememberM3VicoTheme()) {
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = dateFormatter)
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun MonthlyTotalsChart(monthlyTotals: List<MonthlyDepositTotal>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val monthFormatter = remember(monthlyTotals) {
        CartesianValueFormatter { _, value, _ ->
            monthlyTotals.getOrNull(value.toInt())?.month?.let(::formatMonth).orEmpty()
        }
    }

    LaunchedEffect(monthlyTotals) {
        modelProducer.runTransaction {
            lineModel {
                series(monthlyTotals.map { it.totalAmount.toDouble() })
            }
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        ProvideVicoTheme(rememberM3VicoTheme()) {
            CartesianChartHost(
                chart = rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = monthFormatter)
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ReportDepositCard(deposit: DepositReportItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = deposit.goalName, style = MaterialTheme.typography.titleMedium)
            Text(text = deposit.amount.toPlainString(), modifier = Modifier.padding(top = 4.dp))
            Text(text = formatDate(deposit.createdAt), modifier = Modifier.padding(top = 4.dp))
            deposit.note?.takeIf { it.isNotBlank() }?.let { note ->
                Text(text = note, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

private fun formatDateForRequest(day: Int, month: Int, year: Int): String {
    return "%04d-%02d-%02d".format(year, month, day)
}

private fun formatDateForDisplay(day: Int, month: Int, year: Int): String {
    return "%02d.%02d.%04d".format(day, month, year)
}

private fun formatDate(date: String): String {
    val parts = date.substringBefore("T").split("-")
    return if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else date
}

private fun formatMonth(month: String): String {
    return when (month.substringAfter("-")) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "Maj"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Avg"
        "09" -> "Sep"
        "10" -> "Okt"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> month
    }
}
