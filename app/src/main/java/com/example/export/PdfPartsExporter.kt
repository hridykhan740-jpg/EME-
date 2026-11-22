package com.example.export

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.R
import com.example.local.LocalPdfCartItemEntity
import com.example.model.UserVehicle
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.Date

object PdfPartsExporter {

  private const val PAGE_WIDTH = 595
  private const val PAGE_HEIGHT = 842
  private const val MARGIN = 36f

  fun exportAndShare(
    context: Context,
    vehicle: UserVehicle?,
    items: List<LocalPdfCartItemEntity>,
  ): Result<File> {
    if (items.isEmpty()) {
      return Result.failure(IllegalStateException("Add at least one assembly or sub-part to export a PDF."))
    }
    return runCatching {
      val exportsDir = File(context.cacheDir, "exports").apply { mkdirs() }
      val safeSlug = (vehicle?.model ?: "Vehicle").replace(Regex("[^A-Za-z0-9]"), "_")
      val pdfFile = File(exportsDir, "EME_Parts_List_${safeSlug}_${System.currentTimeMillis()}.pdf")

      val document = PdfDocument()
      val titlePaint = Paint().apply {
        color = Color.parseColor("#0F172A")
        textSize = 16f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }
      val brandPaint = Paint().apply {
        color = Color.parseColor("#EA580C")
        textSize = 11f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }
      val headerPaint = Paint().apply {
        color = Color.parseColor("#0369A1")
        textSize = 10f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      }
      val bodyPaint = Paint().apply {
        color = Color.parseColor("#1E293B")
        textSize = 9.5f
      }
      val mutedPaint = Paint().apply {
        color = Color.parseColor("#64748B")
        textSize = 8.5f
      }
      val linePaint = Paint().apply {
        color = Color.parseColor("#CBD5E1")
        strokeWidth = 1f
      }

      val rawWatermark = runCatching {
        BitmapFactory.decodeResource(context.resources, R.drawable.eme_crest_watermark)
      }.getOrNull()
      val watermarkBitmap = rawWatermark?.let { Bitmap.createScaledBitmap(it, 260, 260, true) }

      var pageNum = 1
      var page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create())
      var canvas = page.canvas
      drawWatermark(canvas, watermarkBitmap)
      var y = drawHeader(canvas, vehicle, items.size, brandPaint, titlePaint, bodyPaint, mutedPaint, linePaint)

      items.forEachIndexed { idx, item ->
        if (y > PAGE_HEIGHT - 96f) {
          drawFooter(canvas, pageNum, mutedPaint, linePaint)
          document.finishPage(page)
          pageNum++
          page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create())
          canvas = page.canvas
          drawWatermark(canvas, watermarkBitmap)
          y = 48f
        }

        val badge = if (item.isSubComponent) "SUB-PART" else "ASSEMBLY"
        canvas.drawText("${idx + 1}. [$badge • ${item.systemName}] ${item.partName.take(54)}", MARGIN, y, titlePaint.apply { textSize = 10.5f })
        y += 14f
        canvas.drawText(
          "OEM #: ${item.oemNumber}   |   Aftermarket: ${item.aftermarketNumber.take(30)}   |   $${"%.2f".format(item.priceUsd)}",
          MARGIN + 10f,
          y,
          headerPaint,
        )
        y += 13f
        if (!item.parentAssemblyName.isNullOrBlank()) {
          canvas.drawText("Parent Assembly: ${item.parentAssemblyName}", MARGIN + 10f, y, mutedPaint)
          y += 12f
        }
        canvas.drawText("Spec: ${item.specNote.take(78)}", MARGIN + 10f, y, bodyPaint)
        y += 12f
        canvas.drawText("Fitment: ${item.vehicleSummary}", MARGIN + 10f, y, mutedPaint)
        y += 10f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 14f
      }

      val totalCost = items.sumOf { it.priceUsd * it.quantity }
      if (y > PAGE_HEIGHT - 80f) {
        drawFooter(canvas, pageNum, mutedPaint, linePaint)
        document.finishPage(page)
        pageNum++
        page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create())
        canvas = page.canvas
        drawWatermark(canvas, watermarkBitmap)
        y = 54f
      }
      canvas.drawText(
        "Total Parts Selected: ${items.size}    |    Estimated OEM Value: $${"%.2f".format(totalCost)}",
        MARGIN,
        y + 6f,
        titlePaint.apply { textSize = 11.5f },
      )
      drawFooter(canvas, pageNum, mutedPaint, linePaint)
      document.finishPage(page)

      FileOutputStream(pdfFile).use { out -> document.writeTo(out) }
      document.close()

      val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", pdfFile)
      val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "EME AUTOMATIC HELP • OEM Parts List")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }
      val chooser = Intent.createChooser(shareIntent, "Share or Save OEM Parts List PDF").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(chooser)
      pdfFile
    }
  }

  private fun drawHeader(
    canvas: android.graphics.Canvas,
    vehicle: UserVehicle?,
    count: Int,
    brandPaint: Paint,
    titlePaint: Paint,
    bodyPaint: Paint,
    mutedPaint: Paint,
    linePaint: Paint,
  ): Float {
    var y = 40f
    canvas.drawText("EME AUTOMATIC HELP  •  AUTOPULSE OEM PARTS CATALOG", MARGIN, y, brandPaint)
    y += 18f
    canvas.drawText("Verified Vehicle Parts & Assembly Breakdown List", MARGIN, y, titlePaint)
    y += 16f
    val vehLine = vehicle?.let {
      "Vehicle: ${it.year} ${it.brand} ${it.model} (${it.variant}) • Market: ${it.country} (${it.marketCode}) • Odo: ${it.odometerKm} km"
    } ?: "Vehicle: General Catalog Reference"
    canvas.drawText(vehLine.take(92), MARGIN, y, bodyPaint)
    y += 14f
    val dateStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date())
    canvas.drawText("Generated: $dateStr  •  Total Line Items: $count", MARGIN, y, mutedPaint)
    y += 10f
    canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
    return y + 18f
  }

  private fun drawFooter(
    canvas: android.graphics.Canvas,
    pageNum: Int,
    mutedPaint: Paint,
    linePaint: Paint,
  ) {
    val y = PAGE_HEIGHT - 34f
    canvas.drawLine(MARGIN, y - 10f, PAGE_WIDTH - MARGIN, y - 10f, linePaint)
    canvas.drawText("EME AUTOMATIC HELP • Developed by Àbdüllāh Aĺ Hỗŝŝâîň", MARGIN, y, mutedPaint)
    canvas.drawText("Page $pageNum", PAGE_WIDTH - MARGIN - 40f, y, mutedPaint)
  }

  private fun drawWatermark(canvas: android.graphics.Canvas, bitmap: Bitmap?) {
    if (bitmap == null) return
    val size = 260f
    val left = (PAGE_WIDTH - size) / 2f
    val top = (PAGE_HEIGHT - size) / 2f
    val dst = RectF(left, top, left + size, top + size)
    val clipPath = Path().apply {
      addOval(dst, Path.Direction.CW)
    }
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
      alpha = 20
    }
    canvas.save()
    canvas.clipPath(clipPath)
    canvas.drawBitmap(bitmap, null, dst, paint)
    canvas.restore()
  }
}
