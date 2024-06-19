package com.example.homeshield

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.BitmapFactory
import android.util.Base64
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val action: String,
    val commander: String,
    var timestamp: String,
    val photo: String
)

class DeviceHistoryAdapter(private val dataSet: MutableList<Event>) :
    RecyclerView.Adapter<DeviceHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var eventInfoTextView: TextView

        init {
            // Define click listener for the ViewHolder's View
            imageView = view.findViewById(R.id.imageView)
            eventInfoTextView = view.findViewById(R.id.eventInfoTextView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_event, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val event: Event = dataSet[position]

        val decodedString = Base64.decode(event.photo, Base64.DEFAULT)
        // Convert the decoded byte array into a bitmap
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        // Set the bitmap to the ImageView

        viewHolder.imageView.setImageBitmap(decodedByte)
        val text = "ACTION: " + event.action + "\n" + "INITIATED BY: " + event.commander + "\n" + "TIMESTAMP: " + event.timestamp
        viewHolder.eventInfoTextView.text = text
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
