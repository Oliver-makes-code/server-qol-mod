package de.olivermakesco.server_qol_mod;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class ModGamerules {
    public static final GameRules.Key<GameRules.BooleanValue> STACKABLE_ITEMS = createBoolean("qol:16StackableItems", false);
    public static final GameRules.Key<GameRules.BooleanValue> CREEPERS_DESTROY_BLOCKS = createBoolean("qol:creepersDestroyBlocks", true);
    public static final GameRules.Key<GameRules.BooleanValue> ENDERMEN_PICK_UP_BLOCKS = createBoolean("qol:endermenPickUpBlocks", true);
    public static final GameRules.Key<GameRules.BooleanValue> PER_PLAYER_KEEP_INVENTORY = createBoolean("qol:perPlayerKeepInventory", false);
    public static final GameRules.Key<GameRules.BooleanValue> PER_PLAYER_PVP = createBoolean("qol:perPlayerPvp", false);
    public static final GameRules.Key<GameRules.BooleanValue> DRAGON_ALWAYS_DROPS_EGG = createBoolean("qol:dragonAlwaysDropsEgg", false);

    public static GameRules.Key<GameRules.BooleanValue> createBoolean(String name, boolean defaultValue) {
        return GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public static boolean getBoolean(GameRules.Key<GameRules.BooleanValue> rule) {
        return ServerQolMod.serverInstance.getGameRules().getBoolean(rule);
    }

    public static void init() {}
}
