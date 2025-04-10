package moremounts;

import moremounts.items.BatMountItem;
import moremounts.items.CrocodileMountItem;
import moremounts.items.PolarBearMountItem;
import moremounts.items.SpiderMountItem;
import moremounts.mobs.BatMountMob;
import moremounts.mobs.CrocodileMountMob;
import moremounts.mobs.PolarBearMountMob;
import moremounts.mobs.SpiderMountMob;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.MobRegistry;
import necesse.engine.sound.gameSound.GameSound;
import necesse.entity.mobs.hostile.bosses.CryoQueenMob;
import necesse.entity.mobs.hostile.bosses.EvilsProtectorMob;
import necesse.entity.mobs.hostile.bosses.QueenSpiderMob;
import necesse.entity.mobs.hostile.bosses.SwampGuardianHead;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;

@ModEntry
public class MountPack {

    protected static final float MOUNT_DROP_CHANCE = 0.05f;
    public static GameSound toggleNightVisionEffect;

    public void init() {
        System.out.println("We have more mounts!");

        ItemRegistry.registerItem("batmount", new BatMountItem(), 35000, true);
        ItemRegistry.registerItem("spidermount", new SpiderMountItem(), 35000, true);
        //TODO Void Wizard
        ItemRegistry.registerItem("crocodilemount", new CrocodileMountItem(), 35000, true);
        //TODO Ancient Vulture
        //TODO Pirate Captain
        //TODO Reaper
        ItemRegistry.registerItem("polarbearmount", new PolarBearMountItem(), 35000, true);
        //TODO Pest Warden
        //TODO Sage & Grit
        //TODO Fallen Wizard

        MobRegistry.registerMob("batmount", BatMountMob.class, false);
        MobRegistry.registerMob("spidermount", SpiderMountMob.class, false);
        //TODO Void Wizard
        MobRegistry.registerMob("crocodilemount", CrocodileMountMob.class, false);
        //TODO Ancient Vulture
        //TODO Pirate Captain
        //TODO Reaper
        MobRegistry.registerMob("polarbearmount", PolarBearMountMob.class, false);
        //TODO Pest Warden
        //TODO Sage & Grit
        //TODO Fallen Wizard

        EvilsProtectorMob.lootTable.items.add(new ChanceLootItem(MOUNT_DROP_CHANCE, "batmount"));
        QueenSpiderMob.lootTable.items.add(new ChanceLootItem(MOUNT_DROP_CHANCE, "spidermount"));
        //TODO Void Wizard
        SwampGuardianHead.lootTable.items.add(new ChanceLootItem(MOUNT_DROP_CHANCE, "crocodilemount"));
        //TODO Ancient Vulture
        //TODO Pirate Captain
        //TODO Reaper
        CryoQueenMob.lootTable.items.add(new ChanceLootItem(MOUNT_DROP_CHANCE, "polarbearmount"));
        //TODO Pest Warden
        //TODO Sage & Grit
        //TODO Fallen Wizard
    }

    public void initResources() {
        toggleNightVisionEffect = GameSound.fromFile("togglenightvision");

        BatMountMob.texture = GameTexture.fromFile("mobs/batmount");
        BatMountMob.textureShadow = GameTexture.fromFile("mobs/batmount_shadow");
        BatMountMob.textureMask = GameTexture.fromFile("mobs/spidermount_mask");

        SpiderMountMob.texture = GameTexture.fromFile("mobs/spidermount");
        SpiderMountMob.textureShadow = GameTexture.fromFile("mobs/spidermount_shadow");
        SpiderMountMob.textureMask = GameTexture.fromFile("mobs/spidermount_mask");

        CrocodileMountMob.texture = GameTexture.fromFile("mobs/crocodilemount");

        PolarBearMountMob.texture = GameTexture.fromFile("mobs/polarbearmount");
        PolarBearMountMob.textureShadow = GameTexture.fromFile("mobs/polarbearmount_shadow");
    }

    public void postInit() {}

}
