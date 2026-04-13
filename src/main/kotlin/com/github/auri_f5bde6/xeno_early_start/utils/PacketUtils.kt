package com.github.auri_f5bde6.xeno_early_start.utils

import net.minecraft.block.Block
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraftforge.registries.ForgeRegistries

object PacketUtils {
    fun readBlock(buf: PacketByteBuf): Block {
        val id = buf.readIdentifier()
        return ForgeRegistries.BLOCKS.getValue(id) ?: throw IllegalArgumentException("$id is not a valid block")
    }

    fun writeBlock(buf: PacketByteBuf, block: Block) {
        buf.writeIdentifier(
            ForgeRegistries.BLOCKS.getKey(block) ?: throw IllegalArgumentException("Registry returned null for $block?")
        )
    }

    fun <T> readTagKey(buf: PacketByteBuf, registry: RegistryKey<out Registry<T>>): TagKey<T> {
        return TagKey.of(registry, buf.readIdentifier())
    }

    fun <T> writeTagKey(buf: PacketByteBuf, tagKey: TagKey<T>) {
        buf.writeIdentifier(tagKey.id)
    }

    fun readBlockTag(buf: PacketByteBuf): TagKey<Block> {
        return readTagKey(buf, Registries.BLOCK.key)
    }
}