package moremounts.mobs;

import moremounts.MountPack;
import moremounts.utils.NightVision;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.modifiers.ModifierValue;
import necesse.engine.network.Packet;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.entity.mobs.*;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.summon.summonFollowingMob.mountFollowingMob.MountFollowingMob;
import necesse.gfx.PlayerSprite;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.CollisionFilter;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

public class BatMountMob extends MountFollowingMob implements MountAbility {

    public static GameTexture texture;
    public static GameTexture textureShadow;
    public static GameTexture textureMask;

    protected int nightVisionCounter = 1;

    public BatMountMob() {
        super(100);
        setSpeed(110);
        setFriction(1);
        setSwimSpeed(1);
        accelerationMod = 0.8f;
        setKnockbackModifier(0);

        collision = new Rectangle(-10, -7, 20, 14);
        hitBox = new Rectangle(-14, -14, 28, 28);
        selectBox = new Rectangle(-15, -15, 30, 30);
        overrideMountedWaterWalking = true;
    }

    @Override
    public void runMountAbility(PlayerMob player, Packet content) {

        boolean isNight = player.getWorldEntity().isNight();

        if(!isNight) {
            return;
        }

        nightVisionCounter++;

        if(nightVisionCounter % 2 == 0) {
            SoundManager.playSound(MountPack.toggleNightVisionEffect, SoundEffect.globalEffect().volume(0.6f));
        }

    }

    //This method is required by implementing the MountAbility interface
    @Override
    public boolean canRunMountAbility(PlayerMob player, Packet content) {
        return true;
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
    public void dismounted() {
        super.dismounted();

        if (isClient()) {
            NightVision.turnOffNightVision();
        }
    }

    @Override
    public void clientTick() {
        super.clientTick();

        boolean isNight = this.getWorldEntity().isNight();

        if(!isNight) {
            NightVision.turnOffNightVision();
            return;
        };

        if(nightVisionCounter % 2 == 0) {
            NightVision.turnOnNightVision();
        } else {
            NightVision.turnOffNightVision();
        }

    }

    @Override
    public int getFlyingHeight() {
        return 1000;
    }

    @Override
    public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        GameLight light = level.getLightLevel(x / 32, y / 32);
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 40;
        int dir = this.getDir();
        Point sprite = getAnimSprite(x, y, dir);

        drawY += getBobbing(x, y);

        final DrawOptions options = texture.initDraw().sprite(sprite.x, sprite.y, 64).light(light).pos(drawX, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) { }

            @Override
            public void drawBehindRider(TickManager tickManager) {
                options.draw();
            }
        });

        PlayerMob player = (PlayerMob) this.getFollowingMob();

        topList.add(tm -> {
            options.draw();

            if(player != null && isMounted()) {

                DrawOptions playerSprite = PlayerSprite.getDrawOptions(player, x, y, light, camera, null);
                playerSprite.draw();
            }
        });

        addShadowDrawables(topList, level, x, y, light, camera);
    }

    @Override
    public Point getAnimSprite(int x, int y, int dir) {
        return new Point((int)(getWorldEntity().getTime() / getRockSpeed()) % 4, dir % 4);
    }

    public TextureDrawOptions getShadowDrawOptions(int x, int y, GameLight light, GameCamera camera) {
        GameTexture shadowTexture = textureShadow;
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 40;
        int dir = this.getDir();
        drawY += getBobbing(x, y);
        drawY += 70;

        return shadowTexture.initDraw().sprite(0, dir, 64).light(light).pos(drawX, drawY);
    }

    @Override
    public int getRockSpeed() {
        return 500;
    }

    @Override
    public int getWaterRockSpeed() {
        return  500;
    }

    @Override
    public Point getSpriteOffset(int spriteX, int spriteY) {
        Point point = new Point(0, 0);

        if(spriteX == 0 || spriteX == 2) {
            point.y = -1;
        }

        point.x += getRiderDrawXOffset();
        point.y += getRiderDrawYOffset();

        return point;
    }

    @Override
    public int getRiderDrawYOffset() {
        return -8;
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
    public CollisionFilter getLevelCollisionFilter() {

        PlayerMob player = (PlayerMob) getFollowingMob();

        if(player != null) {

            if(player.getLevel() != null && player.getLevel().isCave) {
                return super.getLevelCollisionFilter().overrideFilter(tp -> ((tp.object()).object.isWall || (tp.object()).object.isRock));
            }
        }

        return super.getLevelCollisionFilter().overrideFilter(tp -> ((tp.object()).object.isWall));
    }

    @Override
    public Stream<ModifierValue<?>> getDefaultRiderModifiers() {
        return Stream.of(
                new ModifierValue<>(BuffModifiers.TRAVEL_DISTANCE, 3),
                new ModifierValue<>(BuffModifiers.WATER_WALKING, true)
        );
    }
}
