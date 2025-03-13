package moremounts.mobs;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.summon.summonFollowingMob.mountFollowingMob.MountFollowingMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class CrocodileMountMob extends MountFollowingMob {

    public static GameTexture texture;

    public CrocodileMountMob() {
        super(100);
        setSpeed(50);
        setFriction(1);
        setSwimSpeed(3);

        collision = new Rectangle(-10, -7, 20, 14);
        hitBox = new Rectangle(-14, -14, 28, 28);
        selectBox = new Rectangle(-15, -15, 30, 30);
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
    public Point getSpriteOffset(int spriteX, int spriteY) {
        Point point = new Point(0, 0);

        if (isAccelerating() && (spriteX == 1 || spriteX == 2)) {
            point.y = -5;
        }

        point.x += getRiderDrawXOffset();
        point.y += getRiderDrawYOffset() + 12;

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
}
