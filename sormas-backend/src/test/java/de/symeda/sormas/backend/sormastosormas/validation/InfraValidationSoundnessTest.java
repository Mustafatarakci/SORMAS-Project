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

	private static class DtoTree extends Tree<ResolvedField> {

		public DtoTree(ResolvedField root) {
			super(root);
		}
	}
	private static class Tree<T> {

		T root;
		List<Tree<T>> children;

		public Tree(T root) {
			this.root = root;
		}

		@Override
		public String toString() {
			return printTree(0);
		}

		private static final int indent = 2;

		private String printTree(int currentIndent) {
			String prefix = String.join("", Collections.nCopies(currentIndent, " "));
			StringBuilder s = new StringBuilder(prefix + root);
			for (Tree<T> child : children) {
				s.append("\n").append(child.printTree(currentIndent + indent));
			}
			return s.toString();
		}

	}

	private DtoTree searchDtoTreeForInfra(DtoTree dtoTree) {
		ResolvedField rootField = dtoTree.root;
		List<ResolvedField> childFields = getNewAndRelevantSubfieldsForField(rootField);

		List<Tree<ResolvedField>> children = new ArrayList<>();

		for (ResolvedField childField : childFields) {
			DtoTree childTree = searchDtoTreeForInfra(new DtoTree(childField));
			children.add(childTree);
		}
		dtoTree.children = children;
		return dtoTree;
	}

	private List<ResolvedField> getNewAndRelevantSubfieldsForField(ResolvedField resolvedField) {
		ResolvedType fieldType = resolvedField.getType();

		List<ResolvedField> newFoundFields = new LinkedList<>();

		if (fieldType == null) {
			return newFoundFields;
		}

		newFoundFields = getAllFieldsForType(fieldType);
		//.filter(t -> !alreadySeen.contains(t))
		unpackUtilTypeFields(newFoundFields);
		alreadySeen.addAll(newFoundFields.stream().map(ResolvedField::getType).collect(Collectors.toList()));
		return new ArrayList<>(newFoundFields);
	}

	private void unpackUtilTypeFields(List<ResolvedField> newFoundFields) {
		List<ResolvedField> concurrentModificationFtw = new ArrayList<>();

		for (ResolvedField curField : newFoundFields) {
			ResolvedType curType = curField.getType();
			if (curType.getErasedType().equals(Map.class)) {
				List<ResolvedType> mapParams = curType.getTypeParameters();

				if (!mapParams.isEmpty()
					&& retainFieldType(mapParams.get(0))
					&& retainFieldType(mapParams.get(1))
					&& Collections.disjoint(mapParams, alreadySeen)) {
					//concurrentModificationFtw.addAll(mapParams);
				}
			} else if (curType.getErasedType().equals(List.class)) {
				List<ResolvedType> listParam = curType.getTypeParameters();

				if (!listParam.isEmpty() && retainFieldType(listParam.get(0)) && Collections.disjoint(listParam, alreadySeen)) {
					//concurrentModificationFtw.addAll(listParam);
				}
			}
		}
		newFoundFields.addAll(concurrentModificationFtw);
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
