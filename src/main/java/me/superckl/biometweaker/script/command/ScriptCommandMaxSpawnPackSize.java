package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.EntityEventHandler;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandMaxSpawnPackSize implements IScriptCommand{

	private final IBiomePackage pack;
	private final String entityClass;
	private final int size;

	public ScriptCommandMaxSpawnPackSize(final IBiomePackage pack, final int size) {
		this(pack, null, size);
	}

	@Override
	public void perform() throws Exception {
		if(this.entityClass == null){
			EntityEventHandler.setGlobalPackSize(this.size);
			return;
		}
		Class<?> clazz;
		try{
			clazz = Class.forName(this.entityClass);
		}catch(final Exception e){
			throw new IllegalArgumentException("Failed to load entity class: "+this.entityClass, e);
		}
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final BiomeGenBase biome = it.next();
			final TIntObjectMap<TObjectIntMap<String>>  map = EntityEventHandler.getPackSizes();
			if(!map.containsKey(biome.biomeID))
				map.put(biome.biomeID, new TObjectIntHashMap<String>());
			map.get(biome.biomeID).put(clazz.getName(), this.size);
		}
	}

}
