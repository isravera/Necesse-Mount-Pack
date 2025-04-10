package moremounts.patch;

import moremounts.utils.BossKillRequirement;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.entity.mobs.friendly.human.humanShop.ExoticMerchantHumanMob;
import necesse.entity.mobs.friendly.human.humanShop.SellingShopItem;
import net.bytebuddy.asm.Advice;

@ModConstructorPatch(target = ExoticMerchantHumanMob.class, arguments = {})
public class ExoticMerchantMethodPatch {

    protected static final int MIN_PRICE = 30000;
    protected static final int MAX_PRICE = 50000;

    @Advice.OnMethodExit
    static void onExit(@Advice.This ExoticMerchantHumanMob merchant) {
        merchant.shop.addSellingItem(
            "batmount",
            new SellingShopItem(1, 1)
                .setRandomPrice(MIN_PRICE, MAX_PRICE)
                .addRequirement(new BossKillRequirement("evilsprotector"))
        );

        merchant.shop.addSellingItem(
            "spidermount",
            new SellingShopItem(1, 1)
                .setRandomPrice(MIN_PRICE, MAX_PRICE)
                .addRequirement(new BossKillRequirement("queenspider"))
        );

        merchant.shop.addSellingItem(
            "crocodilemount",
            new SellingShopItem(1, 1)
                .setRandomPrice(MIN_PRICE, MAX_PRICE)
                .addRequirement(new BossKillRequirement("swampguardian"))
        );

        merchant.shop.addSellingItem(
            "polarbearmount",
            new SellingShopItem(1, 1)
                .setRandomPrice(MIN_PRICE, MAX_PRICE)
                .addRequirement(new BossKillRequirement("cryoqueen"))
        );
    }
}
