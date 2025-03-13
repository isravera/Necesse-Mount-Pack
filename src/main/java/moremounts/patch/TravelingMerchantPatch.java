package moremounts.patch;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.friendly.human.humanShop.HumanShop;
import necesse.entity.mobs.friendly.human.humanShop.TravelingMerchantMob;
import necesse.level.maps.levelData.villageShops.ShopItem;
import necesse.level.maps.levelData.villageShops.VillageShopsData;
import net.bytebuddy.asm.Advice;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ModMethodPatch(target = TravelingMerchantMob.class, name = "getShopItems", arguments = {VillageShopsData.class, ServerClient.class})
public class TravelingMerchantPatch {

    protected static final int MIN_PRICE = 30000;
    protected static final int MAX_PRICE = 50000;

    public static Consumer<GameRandom> returnMountList(ServerClient client, final List<ShopItem> list) {
        return new Consumer<GameRandom>() {
            @Override
            public void accept(GameRandom r2) {
                if(client.characterStats().mob_kills.getKills("evilsprotector") > 0) {
                    list.add(ShopItem.item("batmount", r2.getIntBetween(MIN_PRICE, MAX_PRICE)));
                }
                if(client.characterStats().mob_kills.getKills("queenspider") > 0) {
                    list.add(ShopItem.item("spidermount", r2.getIntBetween(MIN_PRICE, MAX_PRICE)));
                }
                if(client.characterStats().mob_kills.getKills("swampguardian") > 0) {
                    list.add(ShopItem.item("crocodilemount", r2.getIntBetween(MIN_PRICE, MAX_PRICE)));
                }
                if(client.characterStats().mob_kills.getKills("cryoqueen") > 0) {
                    list.add(ShopItem.item("polarbearmount", r2.getIntBetween(MIN_PRICE, MAX_PRICE)));
                }
            }
        };
    }

    @Advice.OnMethodExit
    static void onExit(@Advice.This TravelingMerchantMob merchant, @Advice.Return(readOnly = false)ArrayList<ShopItem> list, @Advice.Argument(1) ServerClient client) {
        GameRandom random = new GameRandom(merchant.getShopSeed() + 5L);
        HumanShop.conditionSection(random, random.getChance(6), TravelingMerchantPatch.returnMountList(client, list));
    }
}