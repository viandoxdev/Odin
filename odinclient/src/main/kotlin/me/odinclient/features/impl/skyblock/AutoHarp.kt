package me.odinclient.features.impl.skyblock

import me.odinmain.features.Category
import me.odinmain.features.Module
import me.odinmain.utils.skyblock.LocationUtils.inSkyblock
import me.odinmain.utils.name
import me.odinmain.utils.*
import me.odinmain.utils.skyblock.PlayerUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.init.Blocks
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemBlock
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * Module to automatically do the Melody's Harp minigame.
 *
 * Modified from: https://github.com/FloppaCoding/FloppaClient/blob/master/src/main/kotlin/floppaclient/module/impl/misc/AutoHarp.kt
 *
 * @author Aton, X45k
 */
object AutoHarp : Module(
    "Auto Harp",
    category = Category.SKYBLOCK,
    description = "Automatically Completes Melody's Harp"
){
    private var inHarp = false
    private var lastInv = 0

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (event.gui !is GuiChest || !inSkyblock) return
        val container = (event.gui as GuiChest).inventorySlots
        if (container !is ContainerChest) return
        inHarp = container.name.startsWith("Harp -")
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (!inHarp || mc.thePlayer == null) return
        val container = mc.thePlayer.openContainer ?: return
        if (container !is ContainerChest) return
        val containerChest = mc.thePlayer.openContainer as? ContainerChest ?: return
        if (containerChest.name == "Harp -") {
            inHarp = false
            return
        }
        val newHash = container.inventorySlots.subList(0,36).joinToString("") { it?.stack?.displayName ?: "" }.hashCode()
        if (lastInv == newHash) return
        lastInv = newHash
        for (ii in 0..6) {
            val slot = container.inventorySlots[37 + ii]
            if ((slot.stack?.item as? ItemBlock)?.block === Blocks.quartz_block) {
                PlayerUtils.windowClick(slot.slotNumber, PlayerUtils.ClickType.Left)
                break
            }
        }
    }
}