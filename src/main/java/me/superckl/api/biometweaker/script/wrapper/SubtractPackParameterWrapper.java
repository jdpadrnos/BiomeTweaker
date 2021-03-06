package me.superckl.api.biometweaker.script.wrapper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.pack.MergedBiomesPackage;
import me.superckl.api.biometweaker.script.pack.SubtractBiomesPackage;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.util.ParameterWrapper;

public class SubtractPackParameterWrapper extends ParameterWrapper{

	public SubtractPackParameterWrapper() {
		super(BTParameterTypes.SUBTRACT_BIOMES_PACKAGE, 2, 2, false);
	}

	@Override
	public Pair<Object[], String[]> parseArgs(final ScriptHandler handler, final String... args) throws Exception {
		if(args.length < 2)
			throw new IllegalArgumentException("Must have at least two biome object arguments for subtract operation!");
		final List<IBiomePackage> parsed = Lists.newArrayList();
		final IBiomePackage main = (IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(args[0], handler);
		if(main == null)
			throw new IllegalArgumentException("Must have at least two biome object arguments for subtract operation!");
		String[] toReturn = new String[0];
		for(int i = 1; i < args.length; i++){
			final IBiomePackage obj = (IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(args[i], handler);
			if(obj == null){
				toReturn = new String[args.length-i];
				System.arraycopy(args, i, toReturn, 0, toReturn.length);
				break;
			}
			parsed.add(obj);
		}
		return Pair.of(new Object[] {new SubtractBiomesPackage(main, new MergedBiomesPackage(parsed.toArray(new IBiomePackage[parsed.size()])))}, toReturn);
	}

}
