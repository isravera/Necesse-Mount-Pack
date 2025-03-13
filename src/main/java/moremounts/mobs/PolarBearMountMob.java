package moremounts.mobs;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.summon.summonFollowingMob.mountFollowingMob.MountFollowingMob;
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

public class PolarBearMountMob extends MountFollowingMob {

    public static GameTexture texture;
    public static GameTexture textureShadow;

    public PolarBearMountMob() {
        super(100);
        setSpeed(75);
        setFriction(10);
        setSwimSpeed(1);

        collision = new Rectangle(-20, -20, 40, 40);
        hitBox = new Rectangle(-30, -25, 60, 50);
        selectBox = new Rectangle(-40, -45, 80, 60);
        overrideMountedWaterWalking = false;
    }

    @Override
    public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        GameLight light = level.getLightLevel(x / 32, y / 32);
        int drawX = camera.getDrawX(x) - 64;
        int drawY = camera.getDrawY(y) - 128 + 36;
        int dir = this.getDir();
        Point sprite = this.getAnimSprite(x, y, dir);
        drawY += this.getBobbing(x, y);
        drawY += this.getLevel().getTile(x / 32, y / 32).getMobSinkingAmount(this);

        final DrawOptions options = texture.initDraw().sprite(sprite.x, sprite.y, 128).light(light).pos(drawX - 2, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) { }

            @Override
            public void drawBehindRider(TickManager tickManager) {
                options.draw();
            }
        });
    }

    @Override
    public TextureDrawOptions getShadowDrawOptions(int x, int y, GameLight light, GameCamera camera) {
        int drawX = camera.getDrawX(x) - 64;
        int drawY = camera.getDrawY(y) - 128 + 36;
        drawY += this.getBobbing(x, y);
        return textureShadow.initDraw().sprite(0, this.getDir(), 128).light(light).pos(drawX, drawY);
    }

    @Override
    public CollisionFilter getLevelCollisionFilter() {
        return super.getLevelCollisionFilter().allLiquidTiles();
    }

    @Override
    public Point getSpriteOffset(int spriteX, int spriteY) {
        Point point = new Point(0, 0);

        if (isAccelerating() && (spriteX == 1 || spriteX == 2)) {
            point.y = -5;
        }

        if(spriteX == 0 || spriteX == 2) {
            point.x = -2;
        }

        point.x += getRiderDrawXOffset();
        point.y += getRiderDrawYOffset() + 5;

        return point;
    }

    @Override
    public int getRiderDrawYOffset() {
        PlayerMob player = (PlayerMob) getFollowingMob();

        if (player != null) {
            return -50;
        } else {
            return 0;
        }
    }

    boolean hasBeenBuffed = false;
    @Override
    public void clientTick(){
        super.clientTick();

        if(isMounted() && !hasBeenBuffed) {
            PlayerMob player = (PlayerMob) getFollowingMob();
            int maxHealth = player.getMaxHealth();
            player.setMaxHealth(maxHealth + 50);
            hasBeenBuffed = true;
        }
    }

    @Override
    public void dismounted() {
        if(isClient()) {

            if(hasBeenBuffed){
                PlayerMob player = (PlayerMob) getFollowingMob();
                player.setMaxHealth(player.getMaxHealth() - 50);
                hasBeenBuffed = false;
            }
        }
    }
}
