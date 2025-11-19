package com.cs407.lab09

/**
 * Represents a ball that can move. (No Android UI imports!)
 *
 * Constructor parameters:
 * - backgroundWidth: the width of the background, of type Float
 * - backgroundHeight: the height of the background, of type Float
 * - ballSize: the width/height of the ball, of type Float
 */
class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        // Call reset()
        reset()
    }

    /**
     * Updates the ball's position and velocity based on the given acceleration and time step.
     * (See lab handout for physics equations)
     */
    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if(isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }

        val prevAccX = accX
        val prevAccY = accY

        // equation(1): v1 = v(t1) = v0 + 1/2(a1 + a0)(t1 − t0)
        val newVelocityX = velocityX + 0.5f * (xAcc + prevAccX) * dT
        val newVelocityY = velocityY + 0.5f * (yAcc + prevAccY) * dT

        // equation(2): l = v0 · (t1 − t0) + 1/6(t1 − t0)^2(3a0 + a1)
        val dt2 = dT * dT
        val dx = velocityX * dT + (1f / 6f) * dt2 * (3f * prevAccX + xAcc)
        val dy = velocityY * dT + (1f / 6f) * dt2 * (3f * prevAccY + yAcc)

        posX += dx
        posY += dy

        velocityX = newVelocityX
        velocityY = newVelocityY

        accX = xAcc
        accY = yAcc
    }

    /**
     * Ensures the ball does not move outside the boundaries.
     * When it collides, velocity and acceleration perpendicular to the
     * boundary should be set to 0.
     */
    fun checkBoundaries() {
        // implement the checkBoundaries function
        // (Check all 4 walls: left, right, top, bottom)

        // Left
        if (posX < 0f) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }

        // Right
        val maxX = backgroundWidth - ballSize
        if (posX > maxX) {
            posX = maxX
            velocityX = 0f
            accX = 0f
        }

        // Top
        if (posY < 0f) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }

        // Bottom
        val maxY = backgroundHeight - ballSize
        if (posY > maxY) {
            posY = maxY
            velocityY = 0f
            accY = 0f
        }
    }

    /**
     * Resets the ball to the center of the screen with zero
     * velocity and acceleration.
     */
    fun reset() {
        // implement the reset function
        // (Reset posX, posY, velocityX, velocityY, accX, accY, isFirstUpdate)
        posX = (backgroundWidth - ballSize) / 2f // screen center
        posY = (backgroundHeight - ballSize) / 2f
        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f
        isFirstUpdate = true
    }
}