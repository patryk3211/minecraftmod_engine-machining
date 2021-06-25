package com.enginemachining.items;

import com.enginemachining.armour.*;
import com.enginemachining.EngineMachiningMod;
import com.enginemachining.blocks.ModdedBlocks;
import com.enginemachining.items.nuggets.*;
import com.enginemachining.items.dust.*;
import com.enginemachining.items.ingot.*;
import com.enginemachining.tools.Hammer;
import com.enginemachining.tools.aluminium.*;
import com.enginemachining.tools.copper.*;
import com.enginemachining.tools.silver.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModdedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EngineMachiningMod.MOD_ID);

    public static final RegistryObject<Item> ore_copper = ITEMS.register("ore_copper", () -> new BlockItem(ModdedBlocks.ore_copper.get(), new Item.Properties().tab(ModdedItemGroups.metals)));
    public static final RegistryObject<Item> ore_tin = ITEMS.register("ore_tin", () -> new BlockItem(ModdedBlocks.ore_tin.get(), new Item.Properties().tab(ModdedItemGroups.metals)));
    public static final RegistryObject<Item> ore_aluminium = ITEMS.register("ore_aluminium", () -> new BlockItem(ModdedBlocks.ore_aluminium.get(), new Item.Properties().tab(ModdedItemGroups.metals)));
    public static final RegistryObject<Item> ore_silver = ITEMS.register("ore_silver", () -> new BlockItem(ModdedBlocks.ore_silver.get(), new Item.Properties().tab(ModdedItemGroups.metals)));
    public static final RegistryObject<Item> ore_nickel = ITEMS.register("ore_nickel", () -> new BlockItem(ModdedBlocks.ore_nickel.get(), new Item.Properties().tab(ModdedItemGroups.metals)));
    public static final RegistryObject<Item> ore_lead = ITEMS.register("ore_lead", () -> new BlockItem(ModdedBlocks.ore_lead.get(), new Item.Properties().tab(ModdedItemGroups.metals)));

    public static final RegistryObject<Item> crusher = ITEMS.register("crusher", () -> new BlockItem(ModdedBlocks.crusher.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> energy_wire_copper = ITEMS.register("energy_wire_copper", () -> new BlockItem(ModdedBlocks.energy_wire_copper.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> power_limiter_copper = ITEMS.register("power_limiter_copper", () -> new BlockItem(ModdedBlocks.power_limiter_copper.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> kinetic_generator = ITEMS.register("kinetic_generator", () -> new BlockItem(ModdedBlocks.kinetic_generator.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> infinite_power_source = ITEMS.register("infinite_power_source", () -> new BlockItem(ModdedBlocks.infinite_power_source.get(), new Item.Properties().tab(ModdedItemGroups.misc)));

    public static final RegistryObject<Item> handcrank = ITEMS.register("handcrank", () -> new BlockItem(ModdedBlocks.handcrank.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> ingot_copper = ITEMS.register("ingot_copper", IngotCopper::new);
    public static final RegistryObject<Item> ingot_tin = ITEMS.register("ingot_tin", IngotTin::new);
    public static final RegistryObject<Item> ingot_aluminium = ITEMS.register("ingot_aluminium", IngotAluminium::new);
    public static final RegistryObject<Item> ingot_silver = ITEMS.register("ingot_silver", IngotSilver::new);
    public static final RegistryObject<Item> ingot_nickel = ITEMS.register("ingot_nickel", IngotNickel::new);
    public static final RegistryObject<Item> ingot_lead = ITEMS.register("ingot_lead", IngotLead::new);

    public static final RegistryObject<Item> dust_copper = ITEMS.register("dust_copper", DustCopper::new);
    public static final RegistryObject<Item> dust_tin = ITEMS.register("dust_tin", DustTin::new);
    public static final RegistryObject<Item> dust_aluminium = ITEMS.register("dust_aluminium", DustAluminium::new);
    public static final RegistryObject<Item> dust_silver = ITEMS.register("dust_silver", DustSilver::new);
    public static final RegistryObject<Item> dust_nickel = ITEMS.register("dust_nickel", DustNickel::new);
    public static final RegistryObject<Item> dust_lead = ITEMS.register("dust_lead", DustLead::new);
    public static final RegistryObject<Item> dust_lapis = ITEMS.register("dust_lapis", DustLapis::new);
    public static final RegistryObject<Item> dust_gold = ITEMS.register("dust_gold", DustGold::new);
    public static final RegistryObject<Item> dust_coal = ITEMS.register("dust_coal", DustCoal::new);
    public static final RegistryObject<Item> dust_iron = ITEMS.register("dust_iron", DustIron::new);
    public static final RegistryObject<Item> dust_diamond = ITEMS.register("dust_diamond", DustDiamond::new);

    public static final RegistryObject<Item> pickaxe_copper = ITEMS.register("pickaxe_copper", PickaxeCopper::new);
    public static final RegistryObject<Item> axe_copper = ITEMS.register("axe_copper", AxeCopper::new);
    public static final RegistryObject<Item> sword_copper = ITEMS.register("sword_copper", SwordCopper::new);
    public static final RegistryObject<Item> hoe_copper = ITEMS.register("hoe_copper", HoeCopper::new);
    public static final RegistryObject<Item> shovel_copper = ITEMS.register("shovel_copper", ShovelCopper::new);
    public static final RegistryObject<Item> helmet_copper = ITEMS.register("helmet_copper", HelmetCopper::new);
    public static final RegistryObject<Item> chestplate_copper = ITEMS.register("chestplate_copper", ChestplateCopper::new);
    public static final RegistryObject<Item> leggins_copper = ITEMS.register("leggins_copper", LegginsCopper::new);
    public static final RegistryObject<Item> boots_copper = ITEMS.register("boots_copper", BootsCopper::new);

    public static final RegistryObject<Item> pickaxe_silver = ITEMS.register("pickaxe_silver", PickaxeSilver::new);
    public static final RegistryObject<Item> axe_silver = ITEMS.register("axe_silver", AxeSilver::new);
    public static final RegistryObject<Item> sword_silver = ITEMS.register("sword_silver", SwordSilver::new);
    public static final RegistryObject<Item> hoe_silver = ITEMS.register("hoe_silver", HoeSilver::new);
    public static final RegistryObject<Item> shovel_silver = ITEMS.register("shovel_silver", ShovelSilver::new);
    public static final RegistryObject<Item> helmet_silver = ITEMS.register("helmet_silver", HelmetSilver::new);
    public static final RegistryObject<Item> chestplate_silver = ITEMS.register("chestplate_silver", ChestplateSilver::new);
    public static final RegistryObject<Item> leggins_silver = ITEMS.register("leggins_silver", LegginsSilver::new);
    public static final RegistryObject<Item> boots_silver = ITEMS.register("boots_silver", BootsSilver::new);

    public static final RegistryObject<Item> pickaxe_aluminium = ITEMS.register("pickaxe_aluminium", PickaxeAluminium::new);
    public static final RegistryObject<Item> axe_aluminium = ITEMS.register("axe_aluminium", AxeAluminium::new);
    public static final RegistryObject<Item> sword_aluminium = ITEMS.register("sword_aluminium", SwordAluminium::new);
    public static final RegistryObject<Item> hoe_aluminium = ITEMS.register("hoe_aluminium", HoeAluminium::new);
    public static final RegistryObject<Item> shovel_aluminium = ITEMS.register("shovel_aluminium", ShovelAluminium::new);
    public static final RegistryObject<Item> helmet_aluminium = ITEMS.register("helmet_aluminium", HelmetAluminium::new);
    public static final RegistryObject<Item> chestplate_aluminium = ITEMS.register("chestplate_aluminium", ChestplateAluminium::new);
    public static final RegistryObject<Item> leggins_aluminium = ITEMS.register("leggins_aluminium", LegginsAluminium::new);
    public static final RegistryObject<Item> boots_aluminium = ITEMS.register("boots_aluminium", BootsAluminium::new);

    public static final RegistryObject<Item> nugget_copper = ITEMS.register("nugget_copper", NuggetCopper::new);
    public static final RegistryObject<Item> nugget_tin = ITEMS.register("nugget_tin", NuggetTin::new);
    public static final RegistryObject<Item> nugget_aluminium = ITEMS.register("nugget_aluminium", NuggetAluminium::new);
    public static final RegistryObject<Item> nugget_silver = ITEMS.register("nugget_silver", NuggetSilver::new);
    public static final RegistryObject<Item> nugget_nickel = ITEMS.register("nugget_nickel", NuggetNickel::new);
    public static final RegistryObject<Item> nugget_lead = ITEMS.register("nugget_lead", NuggetLead::new);

    public static final RegistryObject<Item> battery_disposable = ITEMS.register("battery_disposable", BatteryDisposable::new);

    public static final RegistryObject<Item> hammer = ITEMS.register("hammer", Hammer::new);
    public static final RegistryObject<Item> aluminium_can = ITEMS.register("aluminium_can", () -> new Item(new Item.Properties().tab(ModdedItemGroups.misc)));
}
