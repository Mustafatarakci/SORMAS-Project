package de.symeda.sormas.ui.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.explicatis.ext_token_field.Tokenizable;
import com.vaadin.ui.HorizontalLayout;

import de.symeda.sormas.api.statistics.StatisticsGroupingKey;
import de.symeda.sormas.api.utils.DataHelper;

@SuppressWarnings("serial")
public abstract class StatisticsFilterElement extends HorizontalLayout {

	protected StatisticsFilterElement() {
		setMargin(false);
		setSpacing(false);
	}

	public abstract List<TokenizableValue> getSelectedValues();

	protected List<TokenizableValue> createTokens(StatisticsGroupingKey... groupingKeys) {

		List<TokenizableValue> result = new ArrayList<>(groupingKeys.length);
		for (int i = 0; i < groupingKeys.length; i++) {
			result.add(new TokenizableValue(groupingKeys[i], i));
		}

		return result;
	}

	protected List<TokenizableValue> createTokens(Collection<? extends StatisticsGroupingKey> groupingKeys) {
		List<TokenizableValue> result = new ArrayList<>(groupingKeys.size());
		int index = 0;
		for (StatisticsGroupingKey groupingKey : groupingKeys) {
			result.add(new TokenizableValue(groupingKey, index++));
		}

		return result;
	}

	public static class TokenizableValue implements Tokenizable {

		private final Object value;
		private final String stringRepresentation;
		private final long id;

		public TokenizableValue(Object value, long id) {
			this.value = value;
			stringRepresentation = null;
			this.id = id;
		}

		public TokenizableValue(Object value, String stringRepresentation, long id) {
			this.value = value;
			this.stringRepresentation = stringRepresentation;
			this.id = id;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public String getStringValue() {
			if (stringRepresentation != null) {
				return stringRepresentation;
			} else {
				return DataHelper.toStringNullable(value);
			}
		}

		@Override
		public long getIdentifier() {
			return id;
		}

		@Override
		public String toString() {
			return getStringValue();
		}
	}
}
