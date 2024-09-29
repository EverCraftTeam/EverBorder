/*
 * Copyright (c) 2024 EverNet.
 * Tutti i diritti riservati.
 * Progetto: EverCraft - https://evercraft.it/
 * È vietata la riproduzione, distribuzione, trasmissione o modifica di questo contenuto
 * in qualsiasi forma o con qualsiasi mezzo senza l'autorizzazione scritta esplicita di EverNet.
 * Ogni utilizzo non autorizzato di questo materiale, compresa la creazione di opere derivate o copie,
 * sarà perseguito ai sensi della legge vigente.
 */

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.jvm").version(kotlinVersion)
    }
}

rootProject.name = "EverBorder"