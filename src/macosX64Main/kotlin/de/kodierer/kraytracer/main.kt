package de.kodierer.kraytracer

import de.kodierer.kraytracer.base.*
import de.kodierer.kraytracer.objects.*
import de.kodierer.kraytracer.material.*
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fprintf
import kotlin.math.sqrt
import kotlin.random.Random

fun rayToColor(world: Object, ray: Ray, depth: Int ) : Vector {
    val hit : Hit? = world.hit(ray, 0.001f, Float.MAX_VALUE)
    if (hit != null) {
        if (depth < 50) {
            val scatter = hit.material.scatter(ray, hit)
            if (scatter != null) {
                return scatter.attenuation * rayToColor(world, scatter.scattered, depth + 1)
            }
        }
        return Vector()
    } else {
        val unitDirection = ray.direction.unitVector()
        val t = 0.5f * (unitDirection.y + 1.0f)
        return lerp(Vector(1.0f, 1.0f, 1.0f), Vector(0.5f, 0.7f, 1.0f), t)
    }
}

fun main() {
    val width = 200
    val height = 100
    val samples = 128

    val lookFrom = Vector(3f,1f,2f)
    val lookAt = Vector(0f,0f,-1f)
    val distToFocus = (lookFrom - lookAt).length()
    val aperture = 0.2f
    val camera = Camera(lookFrom, lookAt, Vector(0f,1f,0f), 20f, width.toFloat()/height, aperture, distToFocus)

    val world = ObjectList()
    world.add(Sphere(Vector(0f,0f,-1f), 0.5f, Lambert(Vector(0.1f,0.2f,0.5f))))
    world.add(Sphere(Vector(0.0f, -100.5f, -1.0f), 100f, Lambert(Vector(0.8f, 0.8f, 0.0f))))
    world.add(Sphere(Vector(1.0f, 0.0f, -1.0f), 0.5f, Metal(Vector(0.8f, 0.6f, 0.2f), 0.5f)))
    world.add(Sphere(Vector(-1.0f, 0.0f, -1.0f), 0.5f, Dielectric(1.5f)))
    world.add(Sphere(Vector(-1.0f, 0.0f, -1.0f), -0.45f, Dielectric(1.5f)))

    val file = fopen("out.ppm", "w")

    fprintf(file,"P3\n$width $height\n255\n")
    (0 until height).reversed().forEach { y->
        (0 until width).forEach { x ->
            val color = Vector()
            repeat(samples) {
                val u : Float = (x + Random.nextFloat()) / width
                val v : Float = (y + Random.nextFloat()) / height
                color += rayToColor(world, camera.getRay(u, v), 0)
            }
            color /= samples.toFloat()
            color.applyToAll { e -> sqrt(e) }
            color *= 255.9f
            fprintf(file,"${color.toIntString()}\n")
        }
    }
    fclose(file)
}

