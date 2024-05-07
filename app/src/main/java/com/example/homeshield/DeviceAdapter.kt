package com.example.homeshield

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homeshield.databinding.ItemDeviceBinding
import com.example.homeshield.device.Device

class DeviceAdapter(
    var devices: List<Device>
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    inner class DeviceViewHolder(val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDeviceBinding.inflate(layoutInflater, parent, false)
        return DeviceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.binding.apply {
            deviceNameTextView.text = devices[position].name
            deviceStatusTextView.text = devices[position].location
            deviceLocationTextView.text = devices[position].location

        }
    }

}