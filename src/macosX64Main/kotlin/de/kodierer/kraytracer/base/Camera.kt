package de.kodierer.kraytracer.base

import de.kodierer.kraytracer.base.Vector.Companion.randomUnitInDisc
import kotlin.math.PI
import kotlin.math.tan

class Camera(lookFrom: Vector, lookAt: Vector, up: Vector, vfov: Float, aspect: Float, aperture: Float, focusDistance: Float) {

    private var lowerLeftCorner = Vector()
    private var horizontal = Vector()
    private var vertical = Vector()
    private var origin = lookFrom
    private var u = Vector()
    private var v = Vector()
    private var lensRadius = 0f

    init {
        lensRadius = aperture/2
        val theta = (vfov * PI/180f).toFloat()
        val halfHeight = tan(theta/2f)
        val halfWidth = aspect*halfHeight
        val w = (lookFrom-lookAt).makeUnitVector()
        u = (cross(up, w)).makeUnitVector()
        v = cross(w,u)
        lowerLeftCorner = origin - halfWidth*focusDistance*u - halfHeight*focusDistance*v - focusDistance*w
        horizontal = 2*halfWidth*focusDistance*u
        vertical = 2*halfHeight*focusDistance*v
    }

    fun getRay(x : Float, y: Float): Ray {
        val rd = lensRadius * randomUnitInDisc()
        val offset = u * rd.x + v * rd.y
        return Ray(origin + offset, lowerLeftCorner + x*horizontal + y*vertical - origin - offset);
    }

}