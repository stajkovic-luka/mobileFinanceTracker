package com.stajkovicluka.financeapp.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.data.model.DailyDepositTotal
import com.stajkovicluka.financeapp.data.model.DepositReportItem
import com.stajkovicluka.financeapp.data.model.DepositReportResponse
import com.stajkovicluka.financeapp.data.model.MonthlyDepositTotal
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal

// Pravi PDF iz trenutno ucitanog izvestaja i deli ga kroz Android.
object ReportPdfExporter {
    private const val pageWidth = 595
    private const val pageHeight = 842
    private const val margin = 40f

    fun exportAndShare(
        context: Context,
        report: DepositReportResponse,
        averagePerGoal: BigDecimal,
        dailyTotals: List<DailyDepositTotal>,
        monthlyTotals: List<MonthlyDepositTotal>
    ) {
        val document = PdfDocument()
        val file = createReportFile(context, report)

        try {
            drawSummaryPage(document, report, averagePerGoal, dailyTotals, monthlyTotals)
            drawDepositsPages(document, report.deposits)

            FileOutputStream(file).use { output ->
                document.writeTo(output)
            }
        } finally {
            document.close()
        }

        sharePdf(context, file)
    }

    private fun createReportFile(context: Context, report: DepositReportResponse): File {
        val reportsDirectory = File(context.cacheDir, "reports")
        if (!reportsDirectory.exists() && !reportsDirectory.mkdirs()) {
            throw IllegalStateException("Nije moguce napraviti direktorijum za izvestaje.")
        }

        return File(reportsDirectory, "izvestaj_" + report.from + "_" + report.to + ".pdf")
    }

    private fun drawSummaryPage(
        document: PdfDocument,
        report: DepositReportResponse,
        averagePerGoal: BigDecimal,
        dailyTotals: List<DailyDepositTotal>,
        monthlyTotals: List<MonthlyDepositTotal>
    ) {
        val page = document.startPage(newPage(1))
        val canvas = page.canvas
        val titlePaint = textPaint(20f, true)
        val bodyPaint = textPaint(12f)

        canvas.drawText("Izvestaj uplata", margin, 50f, titlePaint)
        canvas.drawText(
            "Period: " + formatDate(report.from) + " - " + formatDate(report.to),
            margin,
            76f,
            bodyPaint
        )
        canvas.drawText(
            "Ukupno ustedjeno: " + report.totalDeposited.toPlainString(),
            margin,
            96f,
            bodyPaint
        )
        canvas.drawText(
            "Prosek po cilju: " + averagePerGoal.toPlainString(),
            margin,
            116f,
            bodyPaint
        )

        drawBarChart(
            canvas = canvas,
            title = "Dnevne uplate",
            values = dailyTotals.map {
                ChartValue(formatShortDate(it.date), it.totalAmount)
            },
            top = 145f
        )
        drawLineChart(
            canvas = canvas,
            title = "Uplate po mesecima",
            values = monthlyTotals.map {
                ChartValue(formatMonth(it.month), it.totalAmount)
            },
            top = 415f
        )

        document.finishPage(page)
    }

    private fun drawDepositsPages(document: PdfDocument, deposits: List<DepositReportItem>) {
        var pageNumber = 2
        var page = document.startPage(newPage(pageNumber))
        var canvas = page.canvas
        var y = drawDepositsHeader(canvas)
        val bodyPaint = textPaint(11f)

        if (deposits.isEmpty()) {
            canvas.drawText("Nema uplata za izabrani period.", margin, y, bodyPaint)
        } else {
            deposits.forEach { deposit ->
                val lines = depositLines(deposit)
                val neededHeight = lines.size * 17f + 10f

                if (y + neededHeight > pageHeight - margin) {
                    document.finishPage(page)
                    pageNumber++
                    page = document.startPage(newPage(pageNumber))
                    canvas = page.canvas
                    y = drawDepositsHeader(canvas)
                }

                lines.forEach { line ->
                    canvas.drawText(line, margin, y, bodyPaint)
                    y += 17f
                }
                y += 10f
            }
        }

        document.finishPage(page)
    }

    private fun drawDepositsHeader(canvas: Canvas): Float {
        canvas.drawText("Uplate u periodu", margin, 50f, textPaint(18f, true))
        return 80f
    }

    private fun depositLines(deposit: DepositReportItem): List<String> {
        val lines = mutableListOf(
            formatDate(deposit.createdAt) + " - " + deposit.goalName + ": " +
                deposit.amount.toPlainString()
        )
        deposit.note?.takeIf { it.isNotBlank() }?.let { note ->
            lines.add("Napomena: " + shorten(note, 70))
        }
        return lines
    }

    private fun drawBarChart(
        canvas: Canvas,
        title: String,
        values: List<ChartValue>,
        top: Float
    ) {
        drawChartTitle(canvas, title, top)
        if (values.isEmpty()) {
            canvas.drawText("Nema uplata za prikaz.", margin, top + 28f, textPaint(11f))
            return
        }

        val chart = chartArea(top)
        drawAxes(canvas, chart)
        val maxAmount = maximumAmount(values)
        val barSpace = chart.width / values.size
        val barWidth = (barSpace * 0.55f).coerceAtLeast(3f)

        values.forEachIndexed { index, value ->
            val height = value.amount.toFloat() / maxAmount.toFloat() * chart.height
            val centerX = chart.left + barSpace * index + barSpace / 2f
            canvas.drawRect(
                centerX - barWidth / 2f,
                chart.bottom - height,
                centerX + barWidth / 2f,
                chart.bottom,
                Paint().apply { color = Color.rgb(54, 112, 73) }
            )
            drawXLabel(canvas, value.label, index, values.size, centerX, chart.bottom)
        }

        drawMaximumLabel(canvas, maxAmount, chart)
    }

    private fun drawLineChart(
        canvas: Canvas,
        title: String,
        values: List<ChartValue>,
        top: Float
    ) {
        drawChartTitle(canvas, title, top)
        if (values.isEmpty()) {
            canvas.drawText("Nema uplata za prikaz.", margin, top + 28f, textPaint(11f))
            return
        }

        val chart = chartArea(top)
        drawAxes(canvas, chart)
        val maxAmount = maximumAmount(values)
        val step = if (values.size == 1) 0f else chart.width / (values.size - 1)
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(198, 111, 42)
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }
        val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(198, 111, 42)
            style = Paint.Style.FILL
        }

        var previousX = 0f
        var previousY = 0f
        values.forEachIndexed { index, value ->
            val x = chart.left + step * index
            val y = chart.bottom - value.amount.toFloat() / maxAmount.toFloat() * chart.height

            if (index > 0) {
                canvas.drawLine(previousX, previousY, x, y, linePaint)
            }
            canvas.drawCircle(x, y, 4f, pointPaint)
            canvas.drawText(value.label, x - 8f, chart.bottom + 16f, textPaint(8f))
            previousX = x
            previousY = y
        }

        drawMaximumLabel(canvas, maxAmount, chart)
    }

    private fun drawChartTitle(canvas: Canvas, title: String, top: Float) {
        canvas.drawText(title, margin, top, textPaint(14f, true))
    }

    private fun drawAxes(canvas: Canvas, chart: ChartArea) {
        val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            strokeWidth = 1.5f
        }
        canvas.drawLine(chart.left, chart.top, chart.left, chart.bottom, axisPaint)
        canvas.drawLine(chart.left, chart.bottom, chart.right, chart.bottom, axisPaint)
        canvas.drawText("0", chart.left - 14f, chart.bottom, textPaint(8f))
    }

    private fun drawXLabel(
        canvas: Canvas,
        label: String,
        index: Int,
        size: Int,
        centerX: Float,
        bottom: Float
    ) {
        val labelStep = ((size - 1) / 4).coerceAtLeast(1)
        if (index % labelStep == 0 || index == size - 1) {
            canvas.drawText(label, centerX - 10f, bottom + 16f, textPaint(8f))
        }
    }

    private fun drawMaximumLabel(canvas: Canvas, maxAmount: BigDecimal, chart: ChartArea) {
        canvas.drawText(maxAmount.toPlainString(), margin, chart.top + 4f, textPaint(8f))
    }

    private fun maximumAmount(values: List<ChartValue>): BigDecimal {
        return values.maxOf { it.amount }.takeIf { it > BigDecimal.ZERO } ?: BigDecimal.ONE
    }

    private fun chartArea(top: Float): ChartArea {
        return ChartArea(
            left = margin + 35f,
            top = top + 24f,
            right = pageWidth - margin,
            bottom = top + 190f
        )
    }

    private fun newPage(pageNumber: Int): PdfDocument.PageInfo {
        return PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    }

    private fun sharePdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.report_share_pdf))
        )
    }

    private fun textPaint(size: Float, bold: Boolean = false): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = size
            typeface = if (bold) android.graphics.Typeface.DEFAULT_BOLD else android.graphics.Typeface.DEFAULT
        }
    }

    private fun formatDate(value: String): String {
        val parts = value.substringBefore("T").split("-")
        return if (parts.size == 3) parts[2] + "." + parts[1] + "." + parts[0] else value
    }

    private fun formatShortDate(value: String): String {
        val parts = value.split("-")
        return if (parts.size == 3) parts[2] + "." + parts[1] else value
    }

    private fun formatMonth(value: String): String {
        return when (value.substringAfter("-")) {
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
            else -> value
        }
    }

    private fun shorten(value: String, maxLength: Int): String {
        return if (value.length > maxLength) value.take(maxLength - 3) + "..." else value
    }

    private data class ChartValue(val label: String, val amount: BigDecimal)

    private data class ChartArea(
        val left: Float,
        val top: Float,
        val right: Float,
        val bottom: Float
    ) {
        val width: Float
            get() = right - left
        val height: Float
            get() = bottom - top
    }
}
