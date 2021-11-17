package de.symeda.sormas.backend.sormastosormas.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.classmate.members.ResolvedConstructor;
import de.symeda.sormas.api.utils.DataHelper;
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

	@Test
	public void testShareCaseValidationIncoming()
		throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

		final ResolvedType resolve = typeResolver.resolve(DtoRootNode.class);
		final ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(resolve, null, null);
		final ResolvedField resolvedField = resolvedTypeWithMembers.getMemberFields()[0];
		DtoTree caseDtoTree = new DtoTree(resolvedField);
		DtoTree walkedTree = walkDtoTree(caseDtoTree);
		List<DtoData[]> paths = extractInfraFieldPaths(walkedTree);

		SormasToSormasCaseDto caseDto = new SormasToSormasCaseDto();

		for (DtoData[] path : paths) {
			ArrayDeque<DtoData> queue = new ArrayDeque<>(Arrays.asList(path));
			injectWrongInfra(caseDto, queue, caseDto);
		}
		System.out.println(caseDto);
	}

	private void injectWrongInfra(SormasToSormasCaseDto caseDto, Queue<DtoData> path, Object parentObject)
		throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

		DtoData current = path.poll();

		if (current == null) {
			return;
		}

		// first elem is always the dto itself, skip it

		if (current.field.getName().equals("dtoUnderTest")) {
			current = path.poll();
			if (current == null) {
				return;
			}
		}

		final ResolvedField currentField = current.field;
		Object child = null;

		List<Field> availableFields = getFields(parentObject);
		final List<Field> collect = availableFields.stream().filter(f -> f.getName().equals(currentField.getName())).collect(Collectors.toList());
		Field injectionPoint = collect.get(0);
		injectionPoint.setAccessible(true);

		child = injectionPoint.get(parentObject);

		if (child == null) {
			if (path.peek() == null) {
				Constructor<?> constructor = currentField.getType().getErasedType().getConstructor(String.class);
				child = constructor.newInstance(DataHelper.createConstantUuid(0));
			} else {
				Constructor<?> constructor = currentField.getType().getErasedType().getConstructor();
				child = constructor.newInstance();
			}

			injectionPoint.set(parentObject, child);
		}

		if (currentField.getType().isInstanceOf(List.class)) {
			List list = (List) child;
			if (list.isEmpty()) {
				ResolvedType tmp = currentField.getType().getTypeParameters().get(0);
				Constructor<?> constructor = tmp.getErasedType().getConstructor();
				child = constructor.newInstance();
				list.add(child);
			} else {
				child = list.get(0);
			}
		}

		parentObject = child;
		injectWrongInfra(caseDto, path, parentObject);

	}

	private <T> List<Field> getFields(T t) {
		List<Field> fields = new ArrayList<>();
		Class<?> clazz = t.getClass();
		while (clazz != Object.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	private static class DtoRootNode {

		SormasToSormasCaseDto dtoUnderTest;

		public DtoRootNode(SormasToSormasCaseDto dtoUnderTest) {
			this.dtoUnderTest = dtoUnderTest;
		}

	}

	private static class DtoTree extends Tree<DtoData> {

		public DtoTree(ResolvedField rootField) {
			super(new DtoData(rootField), rootField.getType().toString());
		}

	}

	private static class DtoData {

		ResolvedField field;
		List<ResolvedConstructor> constructor;

		public DtoData(ResolvedField resolvedField) {
			this.field = resolvedField;
			this.constructor = getAllConstructors(resolvedField.getType());
		}

		private List<ResolvedConstructor> getAllConstructors(ResolvedType type) {
			ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(type, null, null);
			return Arrays.asList(resolvedTypeWithMembers.getConstructors());

		}

		@Override
		public String toString() {
			return field.toString();
		}
	}

	private static class Tree<N> {

		Node<N> root;

		List<Tree<N>> children;

		public Tree(N data, String label) {
			this.root = new Node<>(data, label);
		}

		@Override
		public String toString() {
			return printTree(0);
		}

		private static final int indent = 2;

		private String printTree(int currentIndent) {
			String prefix = String.join("", Collections.nCopies(currentIndent, " "));
			StringBuilder s = new StringBuilder(prefix + root.data + " : " + root.label);
			for (Tree<N> child : children) {
				s.append("\n").append(child.printTree(currentIndent + indent));
			}
			return s.toString();
		}

	}

	private static class Node<N> {

		N data;
		String label;

		public Node(N data, String label) {
			this.data = data;
			this.label = label;
		}
	}

	private List<DtoData[]> extractInfraFieldPaths(Tree<DtoData> dtoTree) {
		List<DtoData[]> ret = new ArrayList<>();
		DtoData pathFromRoot = dtoTree.root.data;
		List<Tree<DtoData>> currentChildren = dtoTree.children;

		for (Tree<DtoData> subtree : currentChildren) {
			if (subtree.children.isEmpty()) {
				// we reached a leaf node
				if (subtree.root.data.field.getType().getErasedType().getName().startsWith("de.symeda.sormas.api.infrastructure.")) {
					ret.add(
						new DtoData[] {
							pathFromRoot,
							subtree.root.data });
				}
			} else {
				// not a leaf, continue search
				List<DtoData[]> childPaths = extractInfraFieldPaths(subtree);
				final List<DtoData[]> collect = childPaths.stream()
					.map(
						p -> Stream.concat(
							Arrays.stream(
								new DtoData[] {
									pathFromRoot }),
							Arrays.stream(p)).toArray(DtoData[]::new))
					.collect(Collectors.toList());
				ret.addAll(collect);
			}
		}
		return ret;
	}

	private DtoTree walkDtoTree(DtoTree dtoTree) {

		final ResolvedType curType = dtoTree.root.data.field.getType();

		if (curType.getErasedType().equals(Map.class)) {
			List<ResolvedType> mapParams = curType.getTypeParameters();
			if (!mapParams.isEmpty() && retainFieldType(mapParams.get(0)) && retainFieldType(mapParams.get(1))) {
				return walkMap(dtoTree, mapParams);
			}
		}

		if (curType.getErasedType().equals(List.class)) {
			List<ResolvedType> listParam = curType.getTypeParameters();
			if (!listParam.isEmpty() && retainFieldType(listParam.get(0))) {
				return walkList(dtoTree, listParam);
			}
		}

		ResolvedField rootField = dtoTree.root.data.field;
		List<ResolvedField> childFields = getAllFieldsForType(rootField.getType());
		List<Tree<DtoData>> children = new ArrayList<>();
		for (ResolvedField childField : childFields) {
			final DtoTree newTree = new DtoTree(childField);
			DtoTree childTree = walkDtoTree(newTree);
			children.add(childTree);
		}

		dtoTree.children = children;
		return dtoTree;
	}

	@NotNull
	private DtoTree walkList(DtoTree dtoTree, List<ResolvedType> listParam) {
		final ResolvedType resolvedElem = typeResolver.resolve(listParam.get(0).getErasedType());
		List<ResolvedField> resolvedElemField = getAllFieldsForType(resolvedElem);
		dtoTree.children = resolvedElemField.stream().map(DtoTree::new).map(this::walkDtoTree).collect(Collectors.toList());
		return dtoTree;
	}

	@NotNull
	private DtoTree walkMap(DtoTree dtoTree, List<ResolvedType> mapParams) {
		final ResolvedType resolvedKey = typeResolver.resolve(mapParams.get(0).getErasedType());
		List<ResolvedField> resolvedKeyFields = getAllFieldsForType(resolvedKey);
		List<Tree<DtoData>> keyChildren = resolvedKeyFields.stream().map(DtoTree::new).map(this::walkDtoTree).collect(Collectors.toList());

		final ResolvedType resolvedValue = typeResolver.resolve(mapParams.get(0).getErasedType());
		List<ResolvedField> resolvedValueFields = getAllFieldsForType(resolvedValue);
		List<Tree<DtoData>> valueChildren = resolvedValueFields.stream().map(DtoTree::new).map(this::walkDtoTree).collect(Collectors.toList());

		dtoTree.children = Stream.concat(keyChildren.stream(), valueChildren.stream()).collect(Collectors.toList());
		return dtoTree;
	}

	private List<ResolvedField> getAllFieldsForType(ResolvedType type) {
		ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(type, null, null);
		ResolvedField[] memberFields = resolvedTypeWithMembers.getMemberFields();
		ResolvedField[] staticFields = resolvedTypeWithMembers.getStaticFields();
		return Stream.concat(Arrays.stream(memberFields), Arrays.stream(staticFields)).filter(this::retainField).collect(Collectors.toList());
	}

	private List<ResolvedField> getAllFieldsForClass(Class<?> clazz) {
		final ResolvedType resolved = typeResolver.resolve(clazz);
		return getAllFieldsForType(resolved);
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
