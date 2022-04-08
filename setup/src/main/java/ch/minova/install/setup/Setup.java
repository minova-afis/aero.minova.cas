package ch.minova.install.setup;

public class Setup extends BaseSetup {
	public static void main(final String[] args) {
		try {
			final BaseSetup me = new Setup();
			me.run(args);
		} catch (final Exception e) {
			System.exit(-1);
		}
	}
}