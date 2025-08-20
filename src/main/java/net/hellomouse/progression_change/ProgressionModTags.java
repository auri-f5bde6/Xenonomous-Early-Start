package net.hellomouse.progression_change;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public class ProgressionModTags {
    public static class Items {
        public static final TagKey<Item> SHARDS = createTag("shards");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ProgressionMod.of(name));
        }
    }
}
