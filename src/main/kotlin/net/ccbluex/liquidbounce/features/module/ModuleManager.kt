/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2016 - 2021 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.config.ConfigSystem
import net.ccbluex.liquidbounce.event.KeyEvent
import net.ccbluex.liquidbounce.event.Listenable
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.features.module.modules.`fun`.ModuleSkinDerp
import net.ccbluex.liquidbounce.features.module.modules.combat.*
import net.ccbluex.liquidbounce.features.module.modules.exploit.*
import net.ccbluex.liquidbounce.features.module.modules.misc.ModuleSpammer
import net.ccbluex.liquidbounce.features.module.modules.movement.*
import net.ccbluex.liquidbounce.features.module.modules.player.*
import net.ccbluex.liquidbounce.features.module.modules.render.*
import net.ccbluex.liquidbounce.features.module.modules.world.ModuleChestAura
import net.ccbluex.liquidbounce.features.module.modules.world.ModuleFastBreak
import net.ccbluex.liquidbounce.features.module.modules.world.ModuleFastPlace
import net.ccbluex.liquidbounce.features.module.modules.world.ModuleTimer
import org.lwjgl.glfw.GLFW

/**
 * A fairly simple module manager
 */
object ModuleManager : Iterable<Module>, Listenable {

    private val modules = mutableListOf<Module>()

    init {
        ConfigSystem.root("modules", modules)
    }

    /**
     * Handle key input for module binds
     */
    val keyHandler = handler<KeyEvent> { ev ->
        if (ev.action == GLFW.GLFW_PRESS) {
            filter { it.bind == ev.key.code } // modules bound to specific key
                .forEach { it.enabled = !it.enabled } // toggle modules
        }
    }

    /**
     * Register inbuilt client modules
     */
    fun registerInbuilt() {
        val builtin = arrayOf(
            ModuleHud,
            ModuleClickGui,
            ModuleFly,
            ModuleVelocity,
            ModuleSpeed,
            ModuleAutoRespawn,
            ModuleTrigger,
            ModuleAutoBow,
            ModuleNametags,
            ModuleBreadcrumbs,
            ModuleItemESP,
            ModuleCriticals,
            ModuleAntiCactus,
            ModuleHitbox,
            ModuleStrafe,
            ModuleEagle,
            ModuleKick,
            ModuleClip,
            ModuleNoFall,
            ModuleAutoLeave,
            ModuleAbortBreaking,
            ModuleMoreCarry,
            ModuleNoPitchLimit,
            ModulePortalMenu,
            ModuleVehicleOneHit,
            ModuleFastPlace,
            ModuleFastBreak,
            ModuleGodMode,
            ModuleDamage,
            ModuleAutoWalk,
            ModuleNoClip,
            ModuleVehicleFly,
            ModuleFreeze,
            ModuleBedWalk,
            ModuleParkour,
            ModuleSuperKnockback,
            ModuleSkinDerp,
            ModuleKillAura,
            ModuleTimer,
            ModuleDisabler,
            ModulePingSpoof,
            ModuleBlink,
            ModuleAntiLevitation,
            ModuleFullBright,
            ModuleForceUnicodeChat,
            ModuleAntiBlind,
            ModuleTraces,
            ModuleSpammer,
            ModuleChestAura,
            ModuleAutoBreak,
            ModuleAutoArmor,
            ModuleInventoryCleaner,
            ModuleChestStealer
        )

        builtin.forEach(this::addModule)
    }

    fun addModule(module: Module) {
        module.initConfigurable()
        module.init()
        modules += module
    }

    /**
     * Allow `ModuleManager += Module` syntax
     */
    operator fun plusAssign(module: Module) {
        addModule(module)
    }

    override fun iterator() = modules.iterator()

}
