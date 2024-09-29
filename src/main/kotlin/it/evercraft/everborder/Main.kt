/*
 * Copyright (c) 2024 EverNet.
 * Tutti i diritti riservati.
 * Progetto: EverCraft - https://evercraft.it/
 * È vietata la riproduzione, distribuzione, trasmissione o modifica di questo contenuto
 * in qualsiasi forma o con qualsiasi mezzo senza l'autorizzazione scritta esplicita di EverNet.
 * Ogni utilizzo non autorizzato di questo materiale, compresa la creazione di opere derivate o copie,
 * sarà perseguito ai sensi della legge vigente.
 */

package it.evercraft.everborder

import dev.unstackss.EverSpigotLogger
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level
import kotlin.properties.Delegates

class Main : JavaPlugin() {

    companion object {
        var plugin: Main by Delegates.notNull()
    }

    private fun initialize() {
        plugin = this
        saveDefaultConfig()
        config.options().copyDefaults(true)
        config.options().parseComments(true)
        EverSpigotLogger.log(Level.INFO, "[EverSpigotPlugin - EverBorder] Plugin abilitato")
        val bstats = config.getBoolean("bStats")
        if(bstats) {
            val metrics = Metrics(plugin, 23489)
            metrics.addCustomChart(SimplePie("kotlin_version") { KotlinVersion.CURRENT.toString() })
            EverSpigotLogger.log(Level.INFO, "[EverSpigotPlugin - EverBorder] Metrics abilitato")
        }
    }

    private fun events() {

    }

    private fun commands() {

    }

    override fun onEnable() {
        initialize()
        events()
        commands()
    }

    override fun onDisable() {
        EverSpigotLogger.log(Level.SEVERE, "[EverSpigotPlugin - EverBorder] Plugin disabilitato")
    }
}
