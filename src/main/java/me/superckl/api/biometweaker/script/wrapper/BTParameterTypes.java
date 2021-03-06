package me.superckl.api.biometweaker.script.wrapper;

import java.util.List;

import me.superckl.api.biometweaker.script.object.BiomePackScriptObject;
import me.superckl.api.biometweaker.script.pack.AllBiomesPackage;
import me.superckl.api.biometweaker.script.pack.AllButBiomesPackage;
import me.superckl.api.biometweaker.script.pack.BasicBiomesPackage;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.pack.IntersectBiomesPackage;
import me.superckl.api.biometweaker.script.pack.SubtractBiomesPackage;
import me.superckl.api.biometweaker.script.pack.TypeBiomesPackage;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.superscript.ScriptHandler;
import me.superckl.api.superscript.object.ScriptObject;
import me.superckl.api.superscript.util.ParameterType;
import me.superckl.api.superscript.util.ParameterTypes;

public class BTParameterTypes {

	public static final ParameterType SPAWN_TYPE = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final String extracted = (String) ParameterTypes.STRING.tryParse(parameter, handler);
			if(extracted != null)
				return SpawnListType.valueOf(extracted);
			return null;
		}
	};

	public static final ParameterType BASIC_BIOMES_PACKAGE = new ParameterType() {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			final List<Integer> ints = (List<Integer>) ParameterTypes.NON_NEG_INTEGERS.tryParse(parameter, handler);
			if(ints.isEmpty()){
				final ScriptObject obj = handler.getObjects().get(parameter);
				if((obj == null) || !(obj instanceof BiomePackScriptObject))
					return null;
				return ((BiomePackScriptObject)obj).getPack();
			}
			final int[] array = new int[ints.size()];
			for(int i = 0; i < array.length; i++)
				array[i] = ints.get(i);
			return new BasicBiomesPackage(array);
		}
	};

	public static final ParameterType TYPE_BIOMES_PACKAGE = new ParameterType(new TypesPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new TypeBiomesPackage((String) ParameterTypes.STRING.tryParse(parameter, handler));
		}
	};

	public static final ParameterType ALL_BIOMES_PACKAGE = new ParameterType(new AllPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) {
			return new AllBiomesPackage();
		}
	};

	public static final ParameterType ALL_BUT_BIOMES_PACKAGE = new ParameterType(new AllButPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new AllButBiomesPackage((IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType INTERSECT_BIOMES_PACKAGE = new ParameterType(new IntersectPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new IntersectBiomesPackage((IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler));
		}
	};

	public static final ParameterType SUBTRACT_BIOMES_PACKAGE = new ParameterType(new SubtractPackParameterWrapper()) {

		@Override
		public Object tryParse(final String parameter, final ScriptHandler handler) throws Exception {
			return new SubtractBiomesPackage((IBiomePackage) BTParameterTypes.BASIC_BIOMES_PACKAGE.tryParse(parameter, handler), new BasicBiomesPackage());
		}
	};

}
