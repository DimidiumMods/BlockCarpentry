package mod.pianomanu.blockcarpentry.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class SlabFrameModelLoader implements IGeometryLoader<SlabFrameModelGeometry> {

    @Override
    public SlabFrameModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new SlabFrameModelGeometry();
    }
}
