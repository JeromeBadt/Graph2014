package com.kabu.graph;

import java.util.ArrayList;
import java.util.List;

import com.kabu.graph.algorithm.IsAcyclic;

/**
 * Diese Subklasse von Graph erlaubt die Spezifikation von Hierarchien.
 * 
 * Hinweis: Hierarchien sind nicht auf Bäume beschränkt, sondern sind gerichtete
 * azyklische Graphen (DAG)! In ihnen sind zwar Kreise verboten, nicht jedoch
 * "Join"-Kanten.
 */
public class TypeHierarchy extends Graph {
	private Vertex root = null;
	private IsAcyclic algIsAcyclic = null;

	public TypeHierarchy() {
		root = createVertex("Thing");
		algIsAcyclic = new IsAcyclic(this);
	}

	public void setRoot(Vertex v) throws GraphException {
		if (contains(v)) {
			root = v;
		} else {
			throw new GraphException("Ecke nicht Teil des Graphen!");
		}
	}

	public Vertex getRoot() {
		return root;
	}

	/**
	 * Anlegen einer Kante zwischen Sub- und Supertyp.
	 */
	public void setIsSubtypeOf(Vertex subType, Vertex superType)
			throws HierarchyException {

		if (subType == null) {
			throw new HierarchyException("Subtype ist null");
		}

		if (!contains(superType) && superType != null) {
			throw new HierarchyException(
					"Bitte geben Sie einen passenden Supertyp an!");
		}
		
		if (getSupertypesOf(subType).contains(superType != null ? superType : getRoot())) {
			return;
		}

		addVertex(subType);

		// Bei fehlendem Supertyp wählen wir die Wurzel als Supertyp
		if (superType == null) {
			connect(subType, root, "is a");
		} else {
			connect(subType, superType, "is a");
		}

		algIsAcyclic.execute();
		if (!algIsAcyclic.isAcyclic()) {
			throw new HierarchyException(
					"Vererbungsfehler. Hierarchie ist nicht azyklisch!");
		}
	}

	/**
	 * Gibt sämtliche Supertypen einer Ecke zurück. Gegenstück von
	 * getDenotationOf(Vertex vertex).
	 */
	public List<Vertex> getSupertypesOf(Vertex vertex)
			throws HierarchyException {
		if (!contains(vertex) || vertex == null) {
			throw new HierarchyException(
					"Ecke nicht in der Typhierarchie enthalten!");
		}

		List<Vertex> supertypeList = new ArrayList<Vertex>();

		// Der gegebene Knoten gehört selbst zur Denotation
		supertypeList.add(vertex);

		List<Vertex> neighbours = vertex.getNeighbours();

		// Rekursiv sämtliche Supertypen "einsammeln"
		for (Vertex neighbour : neighbours) {
			List<Vertex> supertypesOfNeighbour = getSupertypesOf(neighbour);

			for (Vertex supertype : supertypesOfNeighbour) {
				if (!supertypeList.contains(supertype)) {
					supertypeList.add(supertype);
				}
			}
		}

		return supertypeList;
	}

	/**
	 * Feststellen, ob eine Sub-Supertyp-Beziehung besteht.
	 */
	public boolean isSubtypeOf(Vertex subType, Vertex superType)
			throws HierarchyException {

		if (subType == null || superType == null) {
			throw new HierarchyException("Bitte Typen ungleich null angeben");
		}

		// Prüfe einfach, ob superType in der Menge der Supertypen enthalten ist
		List<Vertex> supertypes = getSupertypesOf(subType);

		supertypes.add(getRoot());

		return supertypes.contains(superType);
	}

	/**
	 * Gibt sämtliche Subtypen einer gegebenen Ecke zurueck. Diese entsprechen
	 * sämtlichen Subklassen im Sinne einer Klassenhierarchie.
	 * 
	 * Duden: De|no|ta|ti|on, die; -, -en (Sprachw. begriffliche od.
	 * Sachbedeutung eines Wortes)
	 */
	public List<Vertex> getDenotationOf(Vertex vertex)
			throws HierarchyException {
		if (!contains(vertex) || vertex == null) {
			throw new HierarchyException(
					"Ecke nicht in der Typhierarchie enthalten!");
		}

		List<Vertex> subtypeList = new ArrayList<Vertex>();

		// Der gegebene Knoten gehört selbst zur Denotation
		subtypeList.add(vertex);

		for (Vertex v : getVertices()) {
			List<Vertex> neighbours = v.getNeighbours();
			if (neighbours.contains(vertex)) {
				// Rekursiv sämtliche Subtypen "einsammeln"
				List<Vertex> subtypesOfChild = getDenotationOf(v);

				for (Vertex subtype : subtypesOfChild) {
					if (!subtypeList.contains(subtype)) {
						subtypeList.add(subtype);
					}
				}
			}
		}

		return subtypeList;
	}

	/**
	 * Feststellen, ob eine Super-Subtyp-Beziehung besteht.
	 */
	public boolean isSupertypeOf(Vertex supertype, Vertex subtype)
			throws HierarchyException {
		return isSubtypeOf(subtype, supertype);
	}

	/**
	 * Feststellen, ob eine echte Super-Subtyp-Beziehung besteht. D.h., ob
	 * Super- und Subtyp ungleich sind.
	 */
	public boolean isProperSupertypeOf(Vertex supertype, Vertex subtype)
			throws HierarchyException {
		return !supertype.equals(subtype) && isSupertypeOf(supertype, subtype);
	}

	/**
	 * Feststellen, ob eine echte Sub-Supertyp-Beziehung besteht. D.h., ob
	 * Super- und Subtyp ungleich sind.
	 */
	public boolean isProperSubtypeOf(Vertex subtype, Vertex supertype)
			throws HierarchyException {
		return !supertype.equals(subtype) && isSubtypeOf(subtype, supertype);
	}
}
