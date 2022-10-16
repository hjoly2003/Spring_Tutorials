package com.bezkoder.spring.jpa.postgresql.model;

import javax.persistence.*;

/**
 * [N]:jpa - The {@code @Entity} annotation marks this class as a model. The {@code @Table, @Id & @Column} annotations map this class to the corresponding DB table.
 */
@Entity
@Table(name = "tutorials")
public class Tutorial {

	/** 
	 * [N]:jpa - The {@code @Id} annotation marks this field as a primary key. 
	 * [N]:jpa - The {@code @GeneratedValue} annotation is used to define generation strategy for the primary key. GenerationType.AUTO means Auto Increment field.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "published")
	private boolean published;

	public Tutorial() {

	}

	public Tutorial(String title, String description, boolean published) {
		this.title = title;
		this.description = description;
		this.published = published;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean isPublished) {
		this.published = isPublished;
	}

	@Override
	public String toString() {
		return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + "]";
	}

}
