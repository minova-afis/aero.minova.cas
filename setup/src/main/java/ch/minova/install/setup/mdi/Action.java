package ch.minova.install.setup.mdi;

public class Action {
	// Reihenfolge nach dem Alphabet
	private String action;
	private String autostart;
	private String detailvisible;
	private String dialog;
	private String documentation;
	private boolean generic;
	private String icon;
	private String id;
	private String param;
	private String shortcut;
	private String supressprint;
	private String visible;
	private String text;

	public Action(final String id, final String icon, final String text) {
		this.id = id;
		setText(text);
		setIcon(icon);
	}

	public Action(final String id, final String icon, final String text, final boolean generic) {
		this.id = id;
		setText(text);
		setIcon(icon);
		setGeneric(generic);
	}

	public Action(final String action) {
		this.action = action;
	}

	public String getAction() {
		return this.action;
	}

	public String getAutostart() {
		return this.autostart;
	}

	public String getDetailVisible() {
		return this.detailvisible;
	}

	public String getDialog() {
		return this.dialog;
	}

	public String getDocumentation() {
		return this.documentation;
	}

	public boolean getGeneric() {
		return this.generic;
	}

	public String getIcon() {
		return this.icon;
	}

	public String getId() {
		return this.id;
	}

	public String getParam() {
		return this.param;
	}

	public String getShortcut() {
		return this.shortcut;
	}

	public String getSupressPrint() {
		return this.supressprint;
	}

	public String getVisible() {
		return this.visible;
	}

	public String getText() {
		return this.text;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public void setAutostart(final String autostart) {
		this.autostart = autostart;
	}

	public void setDetailvisible(final String detailvisible) {
		this.detailvisible = detailvisible;
	}

	public void setDialog(final String dialog) {
		this.dialog = dialog;
	}

	public void setDocumentation(final String documentation) {
		this.documentation = documentation;
	}

	public void setGeneric(final Boolean generic) {
		this.generic = generic;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setParam(final String param) {
		this.param = param;
	}

	public void setShortcut(final String shortcut) {
		this.shortcut = shortcut;
	}

	public void setSupressprint(final String supressprint) {
		this.supressprint = supressprint;
	}

	public void setVisible(final String visible) {
		this.visible = visible;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "(<" + this.id + ">,<" + this.icon + ">,<" + this.text + ">,<" + this.generic + ">)";
	}

	public void merge(final ch.minova.core.install.ActionDocument.Action action2) {
		this.action = action2.getAction();
		this.autostart = action2.getAutostart();
		this.detailvisible = action2.getDetailVisible();
		this.dialog = action2.getDialog();
		this.generic = action2.getGeneric();
		this.icon = action2.getIcon();
		this.id = action2.getId();
		this.param = action2.getParam();
		this.shortcut = action2.getShortcut();
		this.supressprint = action2.getSupressPrint();
		this.visible = action2.getVisible();
		this.text = action2.getText();
	}
}