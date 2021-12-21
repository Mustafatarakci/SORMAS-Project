package de.symeda.sormas.ui.dashboard.surveillance.components.disease.tile;

import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

import de.symeda.sormas.api.disease.DiseaseBurdenDto;

public class DiseaseTileViewLayout extends CssLayout {

	private static final long serialVersionUID = 6582975657305031105L;

	@Override
	protected String getCss(Component c) {
		return "margin-left: 18px; margin-bottom: 18px;";
	}

	public void refresh(List<DiseaseBurdenDto> diseasesBurden) {
		this.removeAllComponents();

		for (DiseaseBurdenDto diseaseBurden : diseasesBurden) {
			DiseaseTileComponent tile = new DiseaseTileComponent(diseaseBurden);
			tile.setWidth(230, Unit.PIXELS);
			addComponent(tile);
		}
	}
}
