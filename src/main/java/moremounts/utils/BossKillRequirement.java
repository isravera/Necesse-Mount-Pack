package moremounts.utils;

import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.friendly.human.humanShop.HumanShop;
import necesse.entity.mobs.friendly.human.humanShop.SellingShopItem;

public class BossKillRequirement implements SellingShopItem.ShopItemRequirement {
    private final String bossId;

    public BossKillRequirement(String bossId) {
        this.bossId = bossId;
    }

    @Override
    public boolean test(GameRandom gameRandom, ServerClient serverClient, HumanShop humanShop, GameBlackboard gameBlackboard) {
        return serverClient.characterStats().mob_kills.getKills(bossId) > 0;
    }

    @Override
    public SellingShopItem.ShopItemRequirement and(SellingShopItem.ShopItemRequirement other) {
        return SellingShopItem.ShopItemRequirement.super.and(other);
    }
}
