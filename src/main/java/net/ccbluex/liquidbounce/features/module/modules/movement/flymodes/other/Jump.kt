/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.flymodes.other

import net.ccbluex.liquidbounce.event.BlockBBEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly.fakeBlock
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly.spoofGround
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly.startY
import net.ccbluex.liquidbounce.features.module.modules.movement.flymodes.FlyMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.minecraft.init.Blocks.air
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos

object Jump : FlyMode("Jump") {

    override fun onUpdate() {
        if (mc.thePlayer == null) {
            return
        }
        if (mc.thePlayer.onGround && MovementUtils.isMoving) {
            mc.thePlayer.jump()
            if (fakeBlock) {
                val posX = mc.thePlayer.posX.toInt()
                val posY = mc.thePlayer.posY.toInt()
                val posZ = mc.thePlayer.posZ.toInt()

                val blockPos = BlockPos(posX, posY - 1, posZ) // Place the block below the player's feet
                mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(blockPos, 1, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
            }
        }
    }
    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (spoofGround) {
            if (packet is C03PacketPlayer) {
                packet.onGround = true
            }
        }
    }
        override fun onBB(event: BlockBBEvent) {
        if (event.block == air && event.y.toDouble() < startY) {
            event.boundingBox = AxisAlignedBB.fromBounds(
                event.x.toDouble(),
                event.y.toDouble(),
                event.z.toDouble(),
                event.x.toDouble() + 1,
                startY,
                event.z.toDouble() + 1
            )
        }
    }
}
