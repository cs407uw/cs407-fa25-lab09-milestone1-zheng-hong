package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    // Expose the ball's position as a StateFlow
    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    /**
     * Called by the UI when the game field's size is known.
     */
    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            // Initialize the ball instance
            ball = Ball(
                backgroundWidth = fieldWidth,
                backgroundHeight = fieldHeight,
                ballSize = ballSizePx
            )

            // Update the StateFlow with the initial position
            _ballPosition.value = Offset(ball!!.posX, ball!!.posY)
        }
    }

    /**
     * Called by the SensorEventListener in the UI.
     */
    fun onSensorDataChanged(event: SensorEvent) {
        // Ensure ball is initialized
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            if (lastTimestamp != 0L) {
                // Calculate the time difference (dT) in seconds
                // Hint: event.timestamp is in nanoseconds
                val NS2S = 1.0f / 1000000000.0f
                val dT = (event.timestamp - lastTimestamp) * NS2S

                // Update the ball's position and velocity
                // Hint: The sensor's x and y-axis are inverted
                val xAcc = -event.values[0]
                val yAcc =  event.values[1]
                currentBall.updatePositionAndVelocity(xAcc = xAcc, yAcc = yAcc, dT = dT)

                currentBall.checkBoundaries()

                // Update the StateFlow to notify the UI
                _ballPosition.update { Offset(currentBall.posX, currentBall.posY) }
            }

            // Update the lastTimestamp
            lastTimestamp = event.timestamp
        }
    }

    fun reset() {
        // Reset the ball's state
        ball?.reset()

        // Update the StateFlow with the reset position
        ball?.let {
            _ballPosition.value = Offset(it.posX, it.posY)
        }

        // Reset the lastTimestamp
        lastTimestamp = 0L
    }
}