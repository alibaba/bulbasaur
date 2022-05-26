package com.alibaba.pokemon.bulbasaur.core;

public class ModuleTest {

	static class ModuleA extends Module {
		private static ModuleA self = null;

		public synchronized static ModuleA getInstance() {
			if (self == null) {
				self = new ModuleA();
			}
			return self;
		}

		// @Override
		// public Module[] require() {
		// return new Module[] { ModuleA.getInstance() };
		// }

	}

	static class ModuleB extends Module {
		private static ModuleB self = null;

		public synchronized static ModuleB getInstance() {
			if (self == null) {
				self = new ModuleB();
			}
			return self;
		}

		// @Override
		// public Module[] require() {
		// return new Module[] { ModuleA.getInstance() };
		// }
	}

	static class ModuleC extends Module {
		private static ModuleC self = null;

		public synchronized static ModuleC getInstance() {
			if (self == null) {
				self = new ModuleC();
			}
			return self;
		}

		@Override
		public Module[] require() {
			// ModuleB 是必选
			// return new Module[] { ModuleA.getInstance(),
			// ModuleB.getInstance() };
			// ModuleB 非必选
			return new Module[] { ModuleA.getInstance() };
		}
	}

	static class ModuleD extends Module {
		private static ModuleD self = null;

		public synchronized static ModuleD getInstance() {
			if (self == null) {
				self = new ModuleD();
			}
			return self;
		}

		@Override
		public Module[] require() {
			return new Module[] { ModuleA.getInstance(), ModuleC.getInstance() };
		}

		@Override
		public Module[] optionalRequire() {
			return new Module[] { ModuleB.getInstance() };
		}
	}

}
