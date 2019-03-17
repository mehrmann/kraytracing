package de.kodierer.kraytracer.material

import de.kodierer.kraytracer.base.*

class Lambert(private val albedo : Vector) : Material {
    override fun scatter(ray: Ray, hit: Hit): Scatter? {
        val target = hit.position + hit.normal + Vector.randomInUnitSphere()
        return Scatter(albedo, Ray(hit.position, target - hit.position))
    }

}