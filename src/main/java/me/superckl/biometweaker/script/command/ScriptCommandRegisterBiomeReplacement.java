package me.superckl.biometweaker.script.command;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.config.Config;
import net.minecraft.world.biome.BiomeGenBase;

@RequiredArgsConstructor
public class ScriptCommandRegisterBiomeReplacement implements IScriptCommand{

	private final IBiomePackage pack;
	private final int replaceWith;

	@Override
	public void perform() throws Exception {
		final Iterator<BiomeGenBase> it = this.pack.getIterator();
		while(it.hasNext()){
			final int id = it.next().biomeID;
			BiomeEventHandler.getBiomeReplacements().put(id, this.replaceWith);
			Config.INSTANCE.onTweak(id);
		}
	}

}
