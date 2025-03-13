package moremounts.items;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.mountItem.MountItem;

public class BatMountItem extends MountItem {

    public BatMountItem() {
        super("batmount");
        rarity = Rarity.LEGENDARY;
    }

    @Override
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "batmounttip"));
        tooltips.add(Localization.translate("itemtooltip", "batmounttip2"));
        return tooltips;
    }
}