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
package net.ccbluex.liquidbounce.features.tabs

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.config.util.decode
import net.ccbluex.liquidbounce.utils.HttpUtils
import net.ccbluex.liquidbounce.utils.extensions.asText
import net.ccbluex.liquidbounce.utils.extensions.createItem
import net.ccbluex.liquidbounce.utils.logger
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.collection.DefaultedList
import java.util.*

/**
 * LiquidBounce Creative Tabs with useful items and blocks
 *
 * @author kawaiinekololis (@team CCBlueX)
 * @depends FabricAPI (for page buttons)
 */
object Tabs {

    /**
     * Special item group is useful to get blocks or items which you are not able to get without give command
     */
    val special = LiquidsItemGroup(
        "Special",
        icon = {
            ItemStack(Blocks.COMMAND_BLOCK).apply {
                addEnchantment(Enchantments.SOUL_SPEED, 1337)
            }
        },
        items = {
            it.add(ItemStack(Blocks.COMMAND_BLOCK))
            it.add(ItemStack(Items.COMMAND_BLOCK_MINECART))
            it.add(ItemStack(Blocks.BARRIER))
            it.add(ItemStack(Blocks.DRAGON_EGG))
            it.add(ItemStack(Blocks.BROWN_MUSHROOM_BLOCK))
            it.add(ItemStack(Blocks.RED_MUSHROOM_BLOCK))
            it.add(ItemStack(Blocks.FARMLAND))
            it.add(ItemStack(Blocks.SPAWNER))
        }
    ).create()

    /**
     * Exploits item group allows you to get items which are able to exploit bugs (like crash exploits or render issues)
     */
    val exploits = LiquidsItemGroup(
        "Exploits",
        icon = { ItemStack(Items.LINGERING_POTION) },
        items = {
            // TODO: Add exploits
            // it.add(createItem("spawner{BlockEntityTag:{EntityId:\"Painting\"}}", 1).setCustomName("§8Test §7| §cmc1.8-mc1.16.4".asText()))
        }
    ).create()

    /**
     * Heads item group allows you to decorate your world with different heads
     */
    private class Head(val name: String, val uuid: UUID, val value: String)
    private class HeadsService(val enabled: Boolean, val url: String)
    private var headsCollection: Array<Head> = runCatching {
        logger.info("Loading heads...")
        // Load head service from cloud
        //  Makes it possible to disable service or change domain in case of an emergency
        val headService: HeadsService = decode(HttpUtils.get("${LiquidBounce.CLIENT_CLOUD}/heads.json"))

        if (headService.enabled) {
            // Load heads from service
            //  Syntax based on HeadDB (headdb.org)
            val heads: HashMap<String, Head> = decode(HttpUtils.get(headService.url))

            heads.map { it.value }
                .toTypedArray()
                .also {
                    logger.info("Successfully loaded ${it.size} heads from the database")
                }
        } else {
            error("Head service has been disabled")
        }
    }.onFailure {
        logger.error("Unable to load heads database", it)
    }.getOrElse { emptyArray() }

    val heads = LiquidsItemGroup(
        "Heads",
        icon = { ItemStack(Items.SKELETON_SKULL) },
        items = {
            it += headsCollection.map { head ->
                createItem("minecraft:player_head{display:{Name:\"{\\\"text\\\":\\\"${head.name}\\\"}\"},SkullOwner:{Id:[I;0,0,0,0],Properties:{textures:[{Value:\"${head.value}\"}]}}}")
            }
        }
    ).create()

}



/**
 * A item group from the client
 */
open class LiquidsItemGroup(val plainName: String, val icon: () -> ItemStack,
                            val items: (items: MutableList<ItemStack>) -> Unit) {

    // Create item group and assign to minecraft groups
    fun create(): ItemGroup {
        // Expand array
        ItemGroup.GROUPS = ItemGroup.GROUPS.copyOf(ItemGroup.GROUPS.size + 1)

        // Build item group
        return object : ItemGroup(GROUPS.size - 1, plainName) {

            override fun getName() = plainName

            override fun getTranslationKey() = plainName.asText()

            override fun shouldRenderName() = true

            override fun createIcon() = icon()

            override fun appendStacks(stacks: DefaultedList<ItemStack>) {
                items(stacks)
            }

        }
    }

}
