package me.cdh

import oshi.hardware.GraphicsCard
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem
import java.lang.Long.numberOfLeadingZeros as leadingZeros

object Stats {
    fun osProperties(os: OperatingSystem): Map<String, String> = buildMap {
        put("OS", "${os.family} ${os.versionInfo.version}")
        put("HostName", os.networkParams.hostName)
        put("UpTime", readableTime(os.systemUptime))
    }

    private fun readableTime(seconds: Long) = "${seconds / 3600}h ${seconds % 3600 / 60}m ${seconds % 3600 % 60}s"

    private fun bytesToReadableSize(bytes: Long): String {
        if (bytes < 1024) return "$bytes B"
        val z = (63 - leadingZeros(bytes)) / 10
        return String.format("%.1f %sB", bytes.toDouble() / (1L shl (z * 10)), " KMGTPE"[z])
    }

    fun hardwareProperties(hardware: HardwareAbstractionLayer): Map<String, String> = buildMap {
        put("CPU", hardware.processor.processorIdentifier.name)
        put("GPU", hardware.graphicsCards.map(GraphicsCard::getName).toTypedArray().joinToString(separator = ","))
        val ramUsed = 1.00 - (hardware.memory.available.toDouble() / hardware.memory.total.toDouble())
        put(
            "RAM", String.format(
                "%s/%s (%.2f%s)",
                bytesToReadableSize(hardware.memory.total - hardware.memory.available).split(" ")[0],
                bytesToReadableSize(hardware.memory.total),
                ramUsed * 100,
                "%"
            )
        )
    }
}