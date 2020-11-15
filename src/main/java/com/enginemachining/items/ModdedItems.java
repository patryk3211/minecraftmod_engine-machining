package com.enginemachining.items;

import com.enginemachining.items.dust.*;
import com.enginemachining.items.ingot.*;
import com.enginemachining.tools.AxeCopper;
import com.enginemachining.tools.HoeCopper;
import com.enginemachining.tools.PickaxeCopper;
import com.enginemachining.tools.SwordCopper;
import net.minecraftforge.registries.ObjectHolder;

public class ModdedItems {
    @ObjectHolder("enginemachining:ingot_copper")
    public static IngotCopper ingot_copper;
    @ObjectHolder("enginemachining:ingot_tin")
    public static IngotTin ingot_tin;
    @ObjectHolder("enginemachining:ingot_aluminium")
    public static IngotAluminium ingot_aluminium;
    @ObjectHolder("enginemachining:ingot_silver")
    public static IngotSilver ingot_silver;
    @ObjectHolder("enginemachining:ingot_nickel")
    public static IngotNickel ingot_nickel;
    @ObjectHolder("enginemachining:ingot_lead")
    public static IngotLead ingot_lead;

    @ObjectHolder("enginemachining:dust_copper")
    public static DustCopper dust_copper;
    @ObjectHolder("enginemachining:dust_tin")
    public static DustTin dust_tin;
    @ObjectHolder("enginemachining:dust_aluminium")
    public static DustAluminium dust_aluminium;
    @ObjectHolder("enginemachining:dust_silver")
    public static DustSilver dust_silver;
    @ObjectHolder("enginemachining:dust_nickel")
    public static DustNickel dust_nickel;
    @ObjectHolder("enginemachining:dust_lead")
    public static DustLead dust_lead;

    @ObjectHolder("enginemachining:pickaxe_copper")
    public static PickaxeCopper pickaxe_copper;
    @ObjectHolder("enginemachining:axe_copper")
    public static AxeCopper axe_copper;
    @ObjectHolder("enginemachining:sword_copper")
    public static SwordCopper sword_copper;
    @ObjectHolder("enginemachining:hoe_copper")
    public static HoeCopper hoe_copper;
    @ObjectHolder("enginemachining:helmet_copper")
    public static HoeCopper helmet_copper;
    @ObjectHolder("enginemachining:chestplate_copper")
    public static HoeCopper chestplate_copper;
    @ObjectHolder("enginemachining:leggins_copper")
    public static HoeCopper leggins_copper;

    @ObjectHolder("enginemachining:battery_disposable")
    public static BatteryDisposable battery_disposable;
}
