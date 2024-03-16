package ua.foxminded.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Car implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;
	
	@NonNull
	@Column
	private String objectId;

	@NonNull
	@Column(name = "model")
	private String name;

	@Column
	private int year;

	@ManyToMany
	@JoinTable(inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "car_id", referencedColumnName = "id"))
	private List<Category> category;

	@ManyToOne
	@JoinColumn(name = "maker_id", referencedColumnName = "id")
	private Maker maker;

}