package tondoa.regions.data_storage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;

public class TRegion {
    public final double x, y, z;

    public Identifier biomeIdentifier;

    public Identifier worldIdentifier;


    public String name;

    public TRegion(String name, Vec3d c, Identifier biomeIdentifier, Identifier worldIdentifier) {
        this(name, c.x, c.y, c.z, biomeIdentifier, worldIdentifier);
    }

    public TRegion(String name, double x, double y, double z, Identifier biomeIdentifier, Identifier worldIdentifier) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.name = name;
        this.biomeIdentifier = biomeIdentifier;
        this.worldIdentifier = worldIdentifier;
    }


    public Text getText() {
        return Text.literal(String.format("%s [%.3f/%.3f/%.3f]", name, x, y, z));
    }

    public Text getTranslatedBiome() {
        return Text.translatable(String.format("biome.%s.%s", biomeIdentifier.getNamespace(), biomeIdentifier.getPath()));
    }

}

