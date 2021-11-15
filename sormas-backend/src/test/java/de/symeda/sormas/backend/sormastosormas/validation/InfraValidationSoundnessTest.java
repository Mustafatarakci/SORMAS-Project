package de.symeda.sormas.backend.sormastosormas.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;

import de.symeda.sormas.api.sormastosormas.caze.SormasToSormasCaseDto;

public class InfraValidationSoundnessTest {

	private static final TypeResolver typeResolver = new TypeResolver();
	private static final MemberResolver memberResolver = new MemberResolver(typeResolver);

	private final Set<Class<?>> ignoreLeaves =
		new HashSet<>(Arrays.asList(Date.class, String.class, Double.class, Float.class, Integer.class, Boolean.class));

	// todo this was 94
	private static final Set<ResolvedType> alreadySeen = new LinkedHashSet<>();

	@Test
	public void testShareCaseValidationIncoming() {

		final ResolvedType resolve = typeResolver.resolve(DtoRootNode.class);
		final ResolvedField resolvedField = memberResolver.resolve(resolve, null, null).getMemberFields()[0];
		DtoTree caseDtoTree = new DtoTree(resolvedField);

		DtoTree walkedTree = searchDtoTreeForInfra(caseDtoTree);
	}

	private static class DtoRootNode {

		SormasToSormasCaseDto dtoUnderTest;

		public DtoRootNode(SormasToSormasCaseDto dtoUnderTest) {
			this.dtoUnderTest = dtoUnderTest;
		}

	}

	private static class ListElemRootNode {

		public ListElemRootNode(Object inner) {
			this.inner = inner;
		}

		Object inner;
	}

	private static class DtoTree extends Tree<ResolvedField> {

		public DtoTree(ResolvedField root) {
			super(root, root.getType().toString());
		}
	}
	private static class Tree<T> {

		T root;
		String label;
		List<Tree<T>> children;

		public Tree(T root, String label) {
			this.root = root;
			this.label = label;
		}

		@Override
		public String toString() {
			return printTree(0);
		}

		private static final int indent = 2;

		private String printTree(int currentIndent) {
			String prefix = String.join("", Collections.nCopies(currentIndent, " "));
			StringBuilder s = new StringBuilder(prefix + root + " : " + label);
			for (Tree<T> child : children) {
				s.append("\n").append(child.printTree(currentIndent + indent));
			}
			return s.toString();
		}

	}

	private DtoTree searchDtoTreeForInfra(DtoTree dtoTree) {

		final ResolvedType curType = dtoTree.root.getType();
		if (curType.getErasedType().equals(Map.class)) {
			List<ResolvedType> mapParams = curType.getTypeParameters();

			if (!mapParams.isEmpty() && retainFieldType(mapParams.get(0)) && retainFieldType(mapParams.get(1))) {

				final ResolvedType resolvedKey = typeResolver.resolve(mapParams.get(0).getErasedType());
				List<ResolvedField> resolvedKeyFields = getAllFieldsForType(resolvedKey);
				List<Tree<ResolvedField>> keyChildren =
					resolvedKeyFields.stream().map(DtoTree::new).map(this::walkDtoTree).collect(Collectors.toList());

				final ResolvedType resolvedValue = typeResolver.resolve(mapParams.get(0).getErasedType());
				List<ResolvedField> resolvedValueFields = getAllFieldsForType(resolvedValue);
				List<Tree<ResolvedField>> valueChildren =
					resolvedValueFields.stream().map(DtoTree::new).map(this::walkDtoTree).collect(Collectors.toList());

				dtoTree.children = Stream.concat(keyChildren.stream(), valueChildren.stream()).collect(Collectors.toList());;
				return dtoTree;

			}

		}
		if (curType.getErasedType().equals(List.class)) {
			List<ResolvedType> listParam = curType.getTypeParameters();
			if (!listParam.isEmpty() && retainFieldType(listParam.get(0))) {
				final ResolvedType resolvedElem = typeResolver.resolve(listParam.get(0).getErasedType());
				List<ResolvedField> resolvedElemField = getAllFieldsForType(resolvedElem);
				dtoTree.children = resolvedElemField.stream().map(DtoTree::new).map(this::walkDtoTree).collect(Collectors.toList());
				return dtoTree;
			}
		}

		return walkDtoTree(dtoTree);

	}

	@NotNull
	private DtoTree walkDtoTree(DtoTree currentNode) {
		ResolvedField rootField = currentNode.root;
		List<ResolvedField> childFields = getRelevantSubfieldsForField(rootField);
		List<Tree<ResolvedField>> children = new ArrayList<>();

		for (ResolvedField childField : childFields) {

			DtoTree childTree = searchDtoTreeForInfra(new DtoTree(childField));
			children.add(childTree);
		}
		currentNode.children = children;
		return currentNode;
	}

	private List<ResolvedField> getRelevantSubfieldsForField(ResolvedField resolvedField) {
		ResolvedType fieldType = resolvedField.getType();

		List<ResolvedField> newFoundFields = new LinkedList<>();

		if (fieldType == null) {
			return newFoundFields;
		}

		newFoundFields = getAllFieldsForType(fieldType);

		alreadySeen.addAll(newFoundFields.stream().map(ResolvedField::getType).collect(Collectors.toList()));
		return new ArrayList<>(newFoundFields);
	}

	// LinkedList implements both, List and Deque
	private LinkedList<ResolvedField> getAllFieldsForType(ResolvedType type) {
		ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(type, null, null);
		ResolvedField[] memberFields = resolvedTypeWithMembers.getMemberFields();
		ResolvedField[] staticFields = resolvedTypeWithMembers.getStaticFields();
		return Stream.concat(Arrays.stream(memberFields), Arrays.stream(staticFields))
			.filter(this::retainField)
			.collect(Collectors.toCollection(LinkedList::new));
	}

	private boolean retainFieldType(ResolvedType type) {
		// todo fix isEnum und typ.isPrimitive
		final boolean ignored = ignoreLeaves.contains(type.getErasedType()) || type.getErasedType().isEnum() || type instanceof ResolvedPrimitiveType;
		return !ignored;
	}

	private boolean retainField(ResolvedField field) {
		return retainFieldType(field.getType());
	}

}
