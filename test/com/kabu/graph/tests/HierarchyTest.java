package com.kabu.graph.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.kabu.graph.HierarchyException;
import com.kabu.graph.TypeHierarchy;
import com.kabu.graph.Vertex;

public class HierarchyTest {
	TypeHierarchy typeHierarchy = new TypeHierarchy();

	Vertex animal = typeHierarchy.createVertex("ANIMAL");
	Vertex cat = typeHierarchy.createVertex("CAT");
	Vertex pet = typeHierarchy.createVertex("PET");
	Vertex petcat = typeHierarchy.createVertex("PETCAT");
	Vertex siamese = typeHierarchy.createVertex("SIAMESE");
	Vertex human = typeHierarchy.createVertex("HUMAN");
	Vertex man = typeHierarchy.createVertex("MAN");
	Vertex woman = typeHierarchy.createVertex("WOMAN");
	Vertex girl = typeHierarchy.createVertex("GIRL");
	Vertex boy = typeHierarchy.createVertex("BOY");
	Vertex employee = typeHierarchy.createVertex("EMPLOYEE");
	Vertex student = typeHierarchy.createVertex("STUDENT");
	Vertex professor = typeHierarchy.createVertex("PROFESSOR");
	Vertex assistant = typeHierarchy.createVertex("ASSISTANT");
	Vertex hiwi = typeHierarchy.createVertex("HIWI");

	public HierarchyTest() {
		try {
			typeHierarchy.setIsSubtypeOf(animal, null);
			typeHierarchy.setIsSubtypeOf(cat, animal);
			typeHierarchy.setIsSubtypeOf(pet, animal);
			typeHierarchy.setIsSubtypeOf(petcat, cat);
			typeHierarchy.setIsSubtypeOf(petcat, pet);
			typeHierarchy.setIsSubtypeOf(siamese, petcat);

			typeHierarchy.setIsSubtypeOf(human, null);
			typeHierarchy.setIsSubtypeOf(man, human);
			typeHierarchy.setIsSubtypeOf(woman, human);
			typeHierarchy.setIsSubtypeOf(girl, woman);
			typeHierarchy.setIsSubtypeOf(boy, man);

			typeHierarchy.setIsSubtypeOf(employee, human);
			typeHierarchy.setIsSubtypeOf(professor, employee);
			typeHierarchy.setIsSubtypeOf(assistant, employee);
			typeHierarchy.setIsSubtypeOf(student, human);
			typeHierarchy.setIsSubtypeOf(hiwi, student);
			typeHierarchy.setIsSubtypeOf(hiwi, employee);
		} catch (HierarchyException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testGetDenotationOf() {
		try {
			assertThat(typeHierarchy.getDenotationOf(typeHierarchy.getRoot()))
					.containsOnly(typeHierarchy.getRoot(), animal, cat, pet,
							petcat, siamese, human, man, woman, girl, boy,
							employee, student, professor, assistant, hiwi);

			assertThat(typeHierarchy.getDenotationOf(cat)).containsOnly(cat,
					petcat, siamese);

			assertThat(typeHierarchy.getDenotationOf(pet)).containsOnly(pet,
					petcat, siamese);

			assertThat(typeHierarchy.getDenotationOf(petcat)).containsOnly(
					petcat, siamese);

			assertThat(typeHierarchy.getDenotationOf(siamese)).containsOnly(
					siamese);

			assertThat(typeHierarchy.getDenotationOf(employee)).containsOnly(
					employee, professor, assistant, hiwi);
		} catch (HierarchyException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testGetSupertypesOf() {
		try {
			assertThat(typeHierarchy.getSupertypesOf(siamese)).containsOnly(
					typeHierarchy.getRoot(), animal, cat, pet, petcat, siamese);

			assertThat(typeHierarchy.getSupertypesOf(petcat)).containsOnly(
					typeHierarchy.getRoot(), animal, cat, pet, petcat);

			assertThat(typeHierarchy.getSupertypesOf(hiwi)).containsOnly(
					typeHierarchy.getRoot(), human, employee, student, hiwi);
		} catch (HierarchyException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testIsSubtypeOf() {
		try {
			assertThat(typeHierarchy.isSubtypeOf(hiwi, typeHierarchy.getRoot()))
					.isTrue();
			assertThat(typeHierarchy.isSubtypeOf(hiwi, employee)).isTrue();
			assertThat(typeHierarchy.isSubtypeOf(hiwi, student)).isTrue();
			assertThat(typeHierarchy.isSubtypeOf(cat, student)).isFalse();
			assertThat(typeHierarchy.isSubtypeOf(professor, girl)).isFalse();
			assertThat(typeHierarchy.isSubtypeOf(professor, employee)).isTrue();
		} catch (HierarchyException e) {
			fail(e.toString());
		}
	}

	@Test(expected = HierarchyException.class)
	public void cyclicTest() throws HierarchyException {
		typeHierarchy.setIsSubtypeOf(pet, petcat);
	}
}
