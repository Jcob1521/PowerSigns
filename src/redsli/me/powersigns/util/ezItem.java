package redsli.me.powersigns.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redslime on 19.11.2016
 */
public class ezItem {

    protected ItemStack item;

    public ezItem(ItemStack itemStack) {
        this.item = itemStack;
    }

    public ezItem(Material material) {
        item = new ItemStack(material, 1);
    }

    public ezItem(Material material, int amount, short data) {
        item = new ItemStack(material, amount, data);
    }

    public static ItemStack getTrue() {
        return new ezItem(Material.LIME_DYE).withDisplayName(" ").get();
    }

    public static ItemStack getFalse() {
        return new ezItem(Material.RED_DYE).withDisplayName(" ").get();
    }

    public static ItemStack getBlank() {
        return new ezItem(Material.BLACK_STAINED_GLASS_PANE).withDisplayName(" ").get();
    }

    public static ItemStack getSkull(String owner, String displayName) {
        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setOwner(owner);
        meta.setDisplayName(displayName);
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        stack.setItemMeta(meta);
        return stack;
    }

    public ezItem withDisplayName(String displayName) {
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(displayName);
        item.setItemMeta(m);
        return this;
    }

    public ezItem withAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ezItem withDurability(short durability) {
        item.setDurability(durability);
        return this;
    }

    public ezItem withData(MaterialData materialData) {
        item.setData(materialData);
        return this;
    }

    public ezItem withLore(String lore) {
        List<String> loreLines = new ArrayList<>();
        ItemMeta m = item.getItemMeta();
        for(String loreLine : lore.split("\n"))
            loreLines.add(loreLine);
        m.setLore(loreLines);
        item.setItemMeta(m);
        return this;
    }

    public ezItem withEnchantment(Enchantment enchantment, int tier) {
        ItemMeta m = item.getItemMeta();
        m.addEnchant(enchantment, tier, true);
        item.setItemMeta(m);
        return this;
    }

    public ezItem withItemFlag(ItemFlag flag) {
        ItemMeta m = item.getItemMeta();
        m.addItemFlags(flag);
        item.setItemMeta(m);
        return this;
    }

    public ItemStack get() {
        return item;
    }
}