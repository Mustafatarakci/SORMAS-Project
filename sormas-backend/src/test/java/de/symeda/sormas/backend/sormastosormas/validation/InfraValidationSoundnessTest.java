package de.symeda.sormas.backend.sormastosormas.validation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import com.fasterxml.classmate.types.ResolvedPrimitiveType;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.sormastosormas.caze.SormasToSormasCaseDto;
import de.symeda.sormas.backend.sormastosormas.SormasToSormasFacadeTest;

@RunWith(MockitoJUnitRunner.class)
public class InfraValidationSoundnessTest extends SormasToSormasFacadeTest {

	@Override
	public void init() {
		super.init();
		getFacilityService().createConstantFacilities();
		getPointOfEntryService().createConstantPointsOfEntry();
	}

	private final Set<Class<?>> ignoreLeaves =
		new HashSet<>(Arrays.asList(Date.class, String.class, Double.class, Float.class, Integer.class, Boolean.class));

	private static final Set<ResolvedType> alreadySeen = new LinkedHashSet<>();

	@Test
	public void testShareCaseValidationIncoming() {
		final MappableRdcf rdcf = createRDCF(false);
		SormasToSormasCaseDto s2sCase = new SormasToSormasCaseDto(createPersonDto(rdcf.centralRdcf), new CaseDataDto());

		searchDtoTreeForInfra(s2sCase.getClass());
	}

	private void searchDtoTreeForInfra(Class<?> clazz) {

		TypeResolver typeResolver = new TypeResolver();
		Queue<ResolvedType> toVisit = getNewAndRelevantMemberTypesOfType(typeResolver.resolve(clazz));

		while (!toVisit.isEmpty()) {
			ResolvedType current = toVisit.poll();
			Queue<ResolvedType> newFoundTypes = getNewAndRelevantMemberTypesOfType(current);
			toVisit.addAll(newFoundTypes);
		}
	}

	private Queue<ResolvedType> getNewAndRelevantMemberTypesOfType(ResolvedType resolvedType) {
		TypeResolver typeResolver = new TypeResolver();
		MemberResolver memberResolver = new MemberResolver(typeResolver);
		Set<ResolvedType> newFoundTypes = new LinkedHashSet<>();
		if (resolvedType == null) {
			return new ArrayDeque<>(newFoundTypes);
		}

		ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(resolvedType, null, null);
		ResolvedField[] memberFields = resolvedTypeWithMembers.getMemberFields();
		ResolvedField[] staticFields = resolvedTypeWithMembers.getStaticFields();
		newFoundTypes = Stream.concat(Arrays.stream(memberFields), Arrays.stream(staticFields))
			.map(ResolvedField::getType)
			.filter(t -> !alreadySeen.contains(t))
			.filter(this::retainFieldType)
			.collect(Collectors.toCollection(LinkedHashSet::new));

		unpackUtilTypes(newFoundTypes);

		alreadySeen.addAll(newFoundTypes);
		return new ArrayDeque<>(newFoundTypes);
	}

	private void unpackUtilTypes(Set<ResolvedType> newFoundTypes) {
		Set<ResolvedType> concurrentModificationFtw = new LinkedHashSet<>();
		for (ResolvedType t : newFoundTypes) {
			if (t.getErasedType().equals(Map.class)) {
				List<ResolvedType> mapParams = t.getTypeParameters();

				if (!mapParams.isEmpty()
					&& retainFieldType(mapParams.get(0))
					&& retainFieldType(mapParams.get(1))
					&& Collections.disjoint(mapParams, alreadySeen)) {
					concurrentModificationFtw.addAll(mapParams);
				}
			}
			if (t.getErasedType().equals(List.class)) {
				List<ResolvedType> listParam = t.getTypeParameters();

				if (!listParam.isEmpty() && retainFieldType(listParam.get(0)) && Collections.disjoint(listParam, alreadySeen)) {
					concurrentModificationFtw.addAll(listParam);
				}
			}
		}
		newFoundTypes.addAll(concurrentModificationFtw);
	}

	private boolean retainFieldType(ResolvedType type) {
		final boolean ignored = ignoreLeaves.contains(type.getErasedType()) || type instanceof ResolvedPrimitiveType;
		return !ignored;
	}

}
