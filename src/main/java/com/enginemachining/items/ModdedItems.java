package com.enginemachining.items;

import com.enginemachining.Armour.BootsCopper;
import com.enginemachining.Armour.ChestplateCopper;
import com.enginemachining.Armour.HelmetCopper;
import com.enginemachining.Armour.LegginsCopper;
import com.enginemachining.items.Nuggetsy.*;
import com.enginemachining.items.dust.*;
import com.enginemachining.items.ingot.*;
import com.enginemachining.tools.AxeCopper;
import com.enginemachining.tools.HoeCopper;
import com.enginemachining.tools.PickaxeCopper;
import com.enginemachining.tools.SwordCopper;
import net.minecraftforge.registries.ObjectHolder;

import javax.swing.event.ChangeEvent;

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
    public static HelmetCopper helmet_copper;
    @ObjectHolder("enginemachining:chestplate_copper")
    public static ChestplateCopper chestplate_copper;
    @ObjectHolder("enginemachining:leggins_copper")
    public static LegginsCopper leggins_copper;
    @ObjectHolder("enginemachining:boots_copper")
    public static BootsCopper boots_copper;

    @ObjectHolder("enginemachining:nugget_aluminium")
    public static NuggetAluminium nugget_aluminium;
    @ObjectHolder("enginemachining:nugget_copper")
    public static NuggetCopper nugget_copper;
    @ObjectHolder("enginemachining:nugget_lead")
    public static NuggetLead nugget_lead;
    @ObjectHolder("enginemachining:nugget_nickel")
    public static NuggetNickel nugget_nickel;
    @ObjectHolder("enginemachining:nugget_tin")
    public static NuggetTin nugget_tin;
    @ObjectHolder("enginemachining:nugget_silver")
    public static NuggetSilver nugget_silver;

    @ObjectHolder("enginemachining:battery_disposable")
    public static BatteryDisposable battery_disposable;
}
