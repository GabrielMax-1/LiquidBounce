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
package net.ccbluex.liquidbounce.features.command.commands.utility

import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.command.builder.CommandBuilder
import net.ccbluex.liquidbounce.utils.chat
import net.ccbluex.liquidbounce.utils.mc
import net.ccbluex.liquidbounce.utils.regular
import net.ccbluex.liquidbounce.utils.variable

import org.lwjgl.glfw.GLFW

object CommandPosition {

    fun createCommand(): Command {
        return CommandBuilder
            .begin("position")
            .alias("pos")
            .handler { command, _ ->
                val position = mc.player!!.blockPos.toShortString()
                chat(regular(command.result("position", variable(position))))
                GLFW.glfwSetClipboardString(mc.window.handle, position)
            }
            .build()
    }

}
