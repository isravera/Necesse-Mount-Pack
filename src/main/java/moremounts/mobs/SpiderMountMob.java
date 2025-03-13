package moremounts.mobs;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.summon.summonFollowingMob.mountFollowingMob.MountFollowingMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.item.Item;
import necesse.level.maps.CollisionFilter;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

public class SpiderMountMob extends MountFollowingMob {

    public static GameTexture texture;
    public static GameTexture textureShadow;
    public static GameTexture textureMask;
    public static boolean canHaveSpiders = false;

    public SpiderMountMob() {
        super(100);
        setSpeed(90);
        setFriction(10);
        setSwimSpeed(1);
        setKnockbackModifier(0.2f);

        collision = new Rectangle(-20, -20, 40, 40);
        hitBox = new Rectangle(-30, -25, 60, 50);
        selectBox = new Rectangle(-40, -45, 80, 60);
        overrideMountedWaterWalking = false;
    }

    @Override
    public void serverTick() {
        super.serverTick();

        if(!isMounted()) {
            moveX = 0;
            moveY = 0;
        }
    }

    @Override
    public void clientTick() {
        super.clientTick();

        if(!isMounted()) {
            moveX = 0;
            moveY = 0;
        }
    }

    @Override
    public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        GameLight light = level.getLightLevel(x / 32, y / 32);
        int drawX = camera.getDrawX(x) - 48;
        int drawY = camera.getDrawY(y) - 60;

        PlayerMob player = (PlayerMob) getFollowingMob();

        if(player != null) {
            Point sprite;

            if(isAccelerating()) {
                sprite = getAnimSprite(x, y, player.getDir());
            } else {
                sprite = getIdleAnimSprite(x + 70, y, player.getDir());
            }

            if(player.getDir() == 0 || player.getDir() == 2) {
                drawX += -18;
            }

            final TextureDrawOptions options = texture.initDraw().sprite(sprite.x, sprite.y, 96).light(light).pos(drawX, drawY);

            list.add(new MobDrawable() {
                @Override
                public void draw(TickManager tickManager) { }

                @Override
                public void drawBehindRider(TickManager tickManager) {
                    options.draw();
                }
            });

            TextureDrawOptionsEnd textureDrawOptionsEnd = textureShadow.initDraw().sprite(sprite.x, sprite.y, 96).light(light).pos(drawX, drawY);
            tileList.add(tm -> textureDrawOptionsEnd.draw());
        }

    }

    @Override
    public Point getAnimSprite(int x, int y, int dir) {
        return new Point((int)(getWorldEntity().getTime() / getRockSpeed()) % 4, dir % 4);
    }

    public Point getIdleAnimSprite(int x, int y, int dir) {
        return new Point((int)(getWorldEntity().getTime() / 2000) % 4, dir % 4);
    }

    @Override
    public int getRockSpeed() {
        return 100;
    }

    @Override
    public int getWaterRockSpeed() {
        return  100;
    }

    @Override
    public CollisionFilter getLevelCollisionFilter() {
        return super.getLevelCollisionFilter().allLiquidTiles();
    }

    @Override
    public Point getSpriteOffset(int spriteX, int spriteY) {
        Point point = new Point(0, 0);

        if (isAccelerating() && (spriteX == 1 || spriteX == 2)) {
            point.y = 2;
        }

        point.x += getRiderDrawXOffset();
        point.y += getRiderDrawYOffset();

        return point;
    }

    @Override
    public int getRiderDrawYOffset() {
        PlayerMob player = (PlayerMob) getFollowingMob();

        if (player != null) {
            return -25;
        } else {
            return 0;
        }
    }

    @Override
    public int getRiderDrawXOffset() {
        PlayerMob player = (PlayerMob) getFollowingMob();
        if (player != null) {
            return player.getDir() == 1 ? 18 : -18;
        } else {
            return 0;
        }
    }

    @Override
    public int getRiderArmSpriteX() {
        return 1;
    }

    @Override
    public int getRiderDir(int startDir) {
        return (startDir) % 4;
    }

    @Override
    public GameTexture getRiderMask() {
        return textureMask;
    }

    @Override
    public int getRiderMaskYOffset() {
        return -9;
    }

    @Override
    public Stream<ModifierValue<?>> getDefaultModifiers() {

        if(isMounted()) {
            return Stream.of(new ModifierValue<>(BuffModifiers.SLOW, 0.0f).max(0.0f));
        }

        return Stream.of();
    }

    @Override
    public Stream<ModifierValue<?>> getDefaultRiderModifiers() {
        PlayerMob following = (PlayerMob) getFollowingMob();

        if (following != null && following.isAttacking) {
            Item item = following.attackSlot.getItem(following.getInv()).item;
            canHaveSpiders = item == ItemRegistry.getItem("spiderstaff");
        }

        if (canHaveSpiders) {
            return Stream.of(
                    new ModifierValue<>(BuffModifiers.MAX_SUMMONS, 10)
            );
        } else {
            return Stream.of(
                    new ModifierValue<>(BuffModifiers.MAX_SUMMONS, 0)
            );
        }

    }
}
