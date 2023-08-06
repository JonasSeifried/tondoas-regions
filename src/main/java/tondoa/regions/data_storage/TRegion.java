package tondoa.regions.data_storage;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class TRegion {
    public final double x, y, z;
    public final String biomeNamespace;

    public final String biomePath;

    public final String name;

    public TRegion(Vec3d coords, Identifier biome, String name) {this(coords.x,coords.y,coords.z, biome, name);}
    public TRegion(TRegion t, String name) {
        this(t.x,t.y,t.z, new Identifier(t.biomeNamespace, t.biomePath), name);}


    public TRegion(double x, double y, double z, Identifier biome, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.biomeNamespace = biome.getNamespace();
        this.biomePath = biome.getPath();
        this.name = name;

    }


    public Text getText() {
        //return Text.translatable(String.format("biome.%s:%s", biomeNamespace, biomePath)).append(String.format(" [%.3f/%.3f/%.3f]", x, y, z));
        return Text.literal(String.format("%s [%.3f/%.3f/%.3f]", name, x, y, z));
    }

    public Text getTranslatedBiome() {
        return Text.translatable(String.format("biome.%s.%s", biomeNamespace, biomePath));
    }

}

