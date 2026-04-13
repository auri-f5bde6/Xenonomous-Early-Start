package com.github.auri_f5bde6.xeno_early_start.utils

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey

data class BlockList(val list: Array<Either<Block, TagKey<Block>>>) {
    fun test(blockState: BlockState): Boolean {
        for (blockOrTag in list) {
            if (blockOrTag.map({ block -> blockState.isOf(block) }, { tagKey -> blockState.isIn(tagKey) })) {
                return true
            }
        }
        return false
    }

    companion object {
        private val BLOCK_ENTRY_CODEC: Codec<Either<Block, TagKey<Block>>> = Codec.either(
            Registries.BLOCK.codec,
            TagKey.codec(Registries.BLOCK.key)
        )
        val CODEC: Codec<BlockList> =
            Codec.either(BLOCK_ENTRY_CODEC, BLOCK_ENTRY_CODEC.listOf())
                .xmap(
                    { entry -> entry.map({ block -> listOf(block) }, { list -> list }) },
                    { entries -> Either.right(entries) }
                ).xmap({ list -> BlockList(list.toTypedArray()) }, { blockList -> blockList.list.toList() })

        fun fromBuf(buf: PacketByteBuf): BlockList {
            val length = buf.readVarInt()
            val list = arrayOfNulls<Either<Block, TagKey<Block>>?>(length)
            for (i in 0..<length) {
                val isTag = buf.readBoolean()
                list[i] = if (isTag) {
                    Either.right(PacketUtils.readBlockTag(buf))
                } else {
                    Either.left(PacketUtils.readBlock(buf))
                }
            }
            @Suppress("unchecked_cast")
            return BlockList(list as Array<Either<Block, TagKey<Block>>>)
        }
    }

    fun toBuf(buf: PacketByteBuf) {
        buf.writeVarInt(list.size)
        for (blockOrTag in list) {
            blockOrTag.map({ block ->
                buf.writeBoolean(false)
                PacketUtils.writeBlock(buf, block)
            }, { tagKey ->
                buf.writeBoolean(true)
                PacketUtils.writeTagKey(buf, tagKey)
            })
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockList

        return list.contentEquals(other.list)
    }

    override fun hashCode(): Int {
        return list.contentHashCode()
    }
}