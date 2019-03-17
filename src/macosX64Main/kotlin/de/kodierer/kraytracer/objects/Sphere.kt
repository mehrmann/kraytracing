package de.kodierer.kraytracer.objects

import de.kodierer.kraytracer.base.*
import kotlin.math.sqrt

class Sphere(private val center: Vector, private val radius: Float, private val material: Material) : Object {

    override fun hit(ray: Ray, tMin: Float, tMax: Float): Hit? {
        val oc = ray.origin - center
        val a = dot(ray.direction, ray.direction)
        val b = dot(oc, ray.direction)
        val c = dot(oc, oc) - radius * radius
        val discriminant = b * b - a * c
        if (discriminant > 0) {
            var temp = (-b - sqrt(discriminant)) / a
            if (temp < tMax && temp > tMin) {
                val position = ray.pointAtParameter(temp)
                val normal = (position - center) / radius
                return Hit(temp, position, normal, material)
            }
            temp = (-b + sqrt(discriminant)) / a
            if (temp < tMax && temp > tMin) {
                val position = ray.pointAtParameter(temp)
                val normal = (position - center) / radius
                return Hit(temp, position, normal, material)
            }
        }
        return null
    }
}