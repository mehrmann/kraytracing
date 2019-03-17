package de.kodierer.kraytracer.material

import de.kodierer.kraytracer.base.*
import de.kodierer.kraytracer.base.Vector.Companion.randomInUnitSphere

class Metal(private val albedo : Vector, private val fuzz: Float) : Material {
    override fun scatter(ray: Ray, hit: Hit): Scatter? {
        val reflected = reflect(ray.direction.unitVector(), hit.normal)
        val scattered = Ray(hit.position, reflected + fuzz * randomInUnitSphere())
        if (dot(scattered.direction, hit.normal) > 0f) {
            return Scatter(albedo, scattered)
        }
        return null
    }
}
