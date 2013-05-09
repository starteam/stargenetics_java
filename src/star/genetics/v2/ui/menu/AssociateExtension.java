package star.genetics.v2.ui.menu;

import java.awt.MenuShortcut;

import star.annotations.SignalComponent;
import star.genetics.Messages;
import utils.JNLPHelpers;

@SignalComponent(extend = MenuItem.class)
public class AssociateExtension extends AssociateExtension_generated
{
	private static final long serialVersionUID = 1L;

	public static boolean hasAssociationCode()
	{
		return JNLPHelpers.supportIntegrationServices();
	}

	@Override
	protected void onAction()
	{
		JNLPHelpers.requestAssociation("application/stargenetics", new String[] { ".sgz", "sg1" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	protected MenuShortcut getMenuShortcut()
	{
		return null;
	}

	@Override
	protected String getMenuName()
	{
		return Messages.getString("AssociateExtension.3"); //$NON-NLS-1$
	}

}
