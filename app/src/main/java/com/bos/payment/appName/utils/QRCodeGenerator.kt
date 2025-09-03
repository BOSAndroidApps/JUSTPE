package  com.bos.payment.appName.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.busRequery.TicketDetailsForGenerateTicket
import com.bos.payment.appName.data.model.travel.flight.FlightTicketDataModel
import com.bos.payment.appName.ui.view.travel.adapter.FlightTicketPassangerDetailsAdapter
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.encoder.QRCode
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


object QRCodeGenerator {

  // for bus ticket
    fun generateQRPdf(
        context: Context,
        bitmap: Bitmap,
        fileName: String,
        tableData: MutableList<MutableList<String?>>,
        headers: Array<String>,
        ticketDetails :TicketDetailsForGenerateTicket,
        response: (Pair<Bitmap, File?>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val finalBitmap = createLayoutWithQR(context, bitmap, tableData, headers, ticketDetails)
            val path = saveBitmapAsPdf(context, finalBitmap,fileName)
            withContext(Dispatchers.Main) {
                response.invoke(Pair(finalBitmap, path))
            }
        }
    }


    private fun createLayoutWithQR(
        context: Context?,
        bitmap: Bitmap?,
        tableData: MutableList<MutableList<String?>>,
        headers: Array<String>,
        ticketDetails :TicketDetailsForGenerateTicket): Bitmap {

        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.bookedticketscreen, null)

        val tableLayout = view.findViewById<TableLayout>(R.id.traveldetailsList)
        val qrImage = view.findViewById<ImageView>(R.id.qrimage)
        val pnrvalue = view.findViewById<TextView>(R.id.pnrvalue)
        val orderId = view.findViewById<TextView>(R.id.booking_RefNo)
        val departureplacetime = view.findViewById<TextView>(R.id.departureplacetime)
        val arrivalplacetime = view.findViewById<TextView>(R.id.arrivalplacetime)
        val departuredatetime = view.findViewById<TextView>(R.id.departuredatetime)
        val arrivaldatetime = view.findViewById<TextView>(R.id.arrivaldatetime)
        val busopertname = view.findViewById<TextView>(R.id.busopertname)
        val driverdetails = view.findViewById<TextView>(R.id.driverdetails)
        val boardingpointtname = view.findViewById<TextView>(R.id.boardingpointtname)
        val reporttime = view.findViewById<TextView>(R.id.reporttime)
        val bustype = view.findViewById<TextView>(R.id.bustype)
        val droppoint = view.findViewById<TextView>(R.id.droppoint)
        val boardtime = view.findViewById<TextView>(R.id.boardtime)
        val basefaretravel = view.findViewById<TextView>(R.id.basefaretravel)
        val operatorgst = view.findViewById<TextView>(R.id.operatorgst)
        val othercharges = view.findViewById<TextView>(R.id.othercharges)
        val conveniencefee = view.findViewById<TextView>(R.id.conveniencefee)
        val totalamount = view.findViewById<TextView>(R.id.totalamount)
        val cancelpolicy = view.findViewById<TextView>(R.id.cancelpolicy)

        // Add header row
        val headerRow = TableRow(context)

        for (header in headers) {
            val textView = TextView(context)
            textView.text = header
            textView.setPadding(16, 16, 16, 16)
            textView.setBackgroundColor(Color.parseColor("#F8F8F8"))
            textView.setTextColor(Color.BLACK)
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.gravity = Gravity.CENTER
            textView.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            headerRow.addView(textView)
        }

        tableLayout.addView(headerRow)

        // Add data rows
        for (row in tableData) {
            val tableRow = TableRow(context)
            for (cell in row) {
                val textView = TextView(context)
                textView.text = cell
                textView.setPadding(16, 16, 16, 16)
                textView.gravity = Gravity.CENTER
                textView.setTextColor(Color.BLACK)
                textView.layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                tableRow.addView(textView)
            }
            tableLayout.addView(tableRow)
        }

        qrImage.setImageBitmap(bitmap)
        pnrvalue.setText(ticketDetails.pnrNumber)
        orderId.setText(ticketDetails.orderId)
        departureplacetime.setText(ticketDetails.departureFrom+" at "+ticketDetails.departureTime)
        arrivalplacetime.setText(ticketDetails.arrivalTo+" at "+ticketDetails.arrivalTime)
        departuredatetime.setText(ticketDetails.departureTime+","+formatDate(ticketDetails.departureDate))
        arrivaldatetime.setText(ticketDetails.arrivalTime+","+formatDate(ticketDetails.arrivalDate))
        busopertname.setText(ticketDetails.busOperatorName)
        driverdetails.setText(ticketDetails.driverContactVehicleNumber)
        boardingpointtname.setText(ticketDetails.boardingPoint)
        reporttime.setText(ticketDetails.reportingTime)
        bustype.setText(ticketDetails.busType)
        droppoint.setText(ticketDetails.droppingPoint)
        boardtime.setText(ticketDetails.boardingTime)
        basefaretravel.setText(ticketDetails.baseFare)
        operatorgst.setText(ticketDetails.gst)
        othercharges.setText(ticketDetails.otherCharge)
        conveniencefee.setText(ticketDetails.convenienceFee)
        totalamount.setText(ticketDetails.totalAmount)
        cancelpolicy.setText("Cancellation Policy : " + ticketDetails.cancelPolicy)


        // Proper layout measure and layout
        val widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        // Create and draw bitmap
        val finalBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)
        view.draw(canvas)

        return finalBitmap
    }


    //.........................................................................................................


    // for air ticket...............................................................................................
    fun generateAirTicket(
        context: Context,
        FlightTicketDetailsList :MutableList<FlightTicketDataModel?> ,
        response: (Pair<Bitmap, File?>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val finalBitmap = createLayoutForAirTicket(context,FlightTicketDetailsList)
            val path = saveBitmapAsPdf(context, finalBitmap,"Airticket")
            withContext(Dispatchers.Main) {
                response.invoke(Pair(finalBitmap, path))
            }
        }
    }


    private fun createLayoutForAirTicket(
        context: Context?,FlightTicketDetailsList :MutableList<FlightTicketDataModel?>):Bitmap {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.layout_airflight_ticket, null)

        var compnayName = view.findViewById<TextView>(R.id.companyname)
        var compnayaddress = view.findViewById<TextView>(R.id.compnayaddress)
        var gstin = view.findViewById<TextView>(R.id.gstin)
        var mailid = view.findViewById<TextView>(R.id.mailid)
        var flightname = view.findViewById<TextView>(R.id.flightname)
        var flighticon = view.findViewById<ImageView>(R.id.flighticon)
        var pnrNo = view.findViewById<TextView>(R.id.pnrNo)
        var refferenceNo = view.findViewById<TextView>(R.id.refferenceNo)
        var Issueddate = view.findViewById<TextView>(R.id.Issueddate)
        var flighticon1 = view.findViewById<ImageView>(R.id.flighticon1)
        var flightno = view.findViewById<TextView>(R.id.flightno)
        var classname = view.findViewById<TextView>(R.id.classname)
        var origin = view.findViewById<TextView>(R.id.origin)
        var departuredatetime = view.findViewById<TextView>(R.id.departuredatetime)
        var destination = view.findViewById<TextView>(R.id.destination)
        var arrivaldatetime = view.findViewById<TextView>(R.id.arrivaldatetime)
        var duration = view.findViewById<TextView>(R.id.duration)
        var status = view.findViewById<TextView>(R.id.status)
        var passangerMobNo = view.findViewById<TextView>(R.id.passangerMobNo)
        var taxesfee = view.findViewById<TextView>(R.id.taxesfee)
        var transactionfee = view.findViewById<TextView>(R.id.transactionfee)
        var grossFare = view.findViewById<TextView>(R.id.grossFare)
        var baseFare = view.findViewById<TextView>(R.id.basefare)
        var passangerDetailsRecyclerview = view.findViewById<RecyclerView>(R.id.passangerDetails)


        compnayName.text=FlightTicketDetailsList[0]!!.compnayName
        compnayaddress.text=FlightTicketDetailsList[0]!!.companyAddress
        gstin.text=FlightTicketDetailsList[0]!!.gstno
        mailid.text=FlightTicketDetailsList[0]!!.mail
        flightname.text=FlightTicketDetailsList[0]!!.flightName
       // flighticon.text=FlightTicketDetailsList[0]!!.flightcode
        pnrNo.text=FlightTicketDetailsList[0]!!.PNR
        refferenceNo.text=FlightTicketDetailsList[0]!!.referenceNo
        Issueddate.text=FlightTicketDetailsList[0]!!.issuedDate
       // flighticon1.text=FlightTicketDetailsList[0]!!.compnayName
        flightno.text=FlightTicketDetailsList[0]!!.flightNo
        classname.text=FlightTicketDetailsList[0]!!.className
        origin.text=FlightTicketDetailsList[0]!!.origin
        departuredatetime.text=FlightTicketDetailsList[0]!!.originDateTime
        arrivaldatetime.text=FlightTicketDetailsList[0]!!.destinationDateTime
        destination.text=FlightTicketDetailsList[0]!!.destination
        duration.text=FlightTicketDetailsList[0]!!.duration
        status.text=FlightTicketDetailsList[0]!!.status
        passangerMobNo.text=FlightTicketDetailsList[0]!!.passangerMobileNo
        taxesfee.text=FlightTicketDetailsList[0]!!.taxesfees.toString()
        transactionfee.text=FlightTicketDetailsList[0]!!.transactionfee.toString()
        grossFare.text=FlightTicketDetailsList[0]!!.GrossFare.toString()
        baseFare.text=FlightTicketDetailsList[0]!!.baseFare.toString()

        var adapter  = FlightTicketPassangerDetailsAdapter(context,FlightTicketDetailsList[0]!!.paxDetails)
        passangerDetailsRecyclerview.adapter = adapter
        adapter.notifyDataSetChanged()

        // Proper layout measure and layout
        val widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        // Create and draw bitmap
        val finalBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)
        view.draw(canvas)

        return finalBitmap
    }

    //................................................................................................................




    private fun saveBitmapAsPdf(context: Context, bitmap: Bitmap,fileName: String): File? {
        // Create a new PdfDocument
        val document = PdfDocument()

        // Create a page info with bitmap dimensions
        val pageInfo = PageInfo.Builder(
            bitmap.width, bitmap.height, 1
        ).create()

        // Start a page
        val page = document.startPage(pageInfo)

        // Draw the bitmap on the page's canvas
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        // Save to external files dir
        val pdfDir = File(context.getExternalFilesDir(null), "pdfs")
        if (!pdfDir.exists()) {
            pdfDir.mkdirs()
        }

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val pdfFile = File(downloadsDir, "$fileName.pdf")

        //val pdfFile = File(pdfDir, "${Calendar.getInstance().timeInMillis}.pdf")

        try {
            FileOutputStream(pdfFile).use { fos ->
                document.writeTo(fos)
            }
            val uri = Uri.fromFile(pdfFile)
            val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
            context.sendBroadcast(scanIntent)
          // Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        document.close()
        return pdfFile
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())

        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }


    fun File.openPdfFile(context: Context) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                this
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }

            context.startActivity(Intent.createChooser(intent, "Open PDF"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
