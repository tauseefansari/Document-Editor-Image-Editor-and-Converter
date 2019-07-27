package com.stark;

import java.util.EventListener;

public interface CloseableTabbedPaneListener extends EventListener {
	public boolean closeTab(int index);

}